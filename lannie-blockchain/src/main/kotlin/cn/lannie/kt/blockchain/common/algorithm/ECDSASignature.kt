package cn.lannie.kt.blockchain.common.algorithm

import com.google.common.base.Objects
import org.bouncycastle.asn1.ASN1InputStream
import org.bouncycastle.asn1.ASN1Integer
import org.bouncycastle.asn1.DERSequenceGenerator
import org.bouncycastle.asn1.DLSequence
import org.bouncycastle.crypto.params.ECDomainParameters
import org.spongycastle.crypto.ec.CustomNamedCurves
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.ClassCastException
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.math.BigInteger

class ECDSASignature {
    var HALF_CURVE_ORDER:BigInteger
    var CURVE:ECDomainParameters
    /**
     * two components of the signature
     */
    var r:BigInteger
    var s:BigInteger

    constructor(r:BigInteger, s:BigInteger, CURVE: ECDomainParameters, HALF_CURVE_ORDER:BigInteger){
        this.r = r
        this.s = s
        this.HALF_CURVE_ORDER = HALF_CURVE_ORDER
        this.CURVE = CURVE
    }
    constructor(CURVE: ECDomainParameters, HALF_CURVE_ORDER:BigInteger){
        this.r = BigInteger.ZERO
        this.s = BigInteger.ZERO
        this.HALF_CURVE_ORDER = HALF_CURVE_ORDER
        this.CURVE = CURVE
    }

    /**
     * Returns true if the S component is "low", that means it is below
     */
    fun isCanonical():Boolean{
        return s.compareTo(HALF_CURVE_ORDER) <= 0
    }

    /**
     * Will automatically adjust the S component to be less than or equal to
     * half the curve order, if necessary. This is required because for
     * every signature (r,s) the signature (r, -s (mod N)) is a valid
     * signature of the same message. However, we dislike the ability to
     * modify the bits of a Bitcoin transaction after it's been signed, as
     * that violates various assumed invariants. Thus in future only one of
     * those forms will be considered legal and the other will be banned.
     */
    fun toCanonicalised():ECDSASignature{
        if(!isCanonical()){
            return ECDSASignature(this.r,this.CURVE.n.subtract(s),this.CURVE ,this.HALF_CURVE_ORDER)
        }else{
            return this
        }
    }

    /**
     * DER is an international standard for serializing data structures
     * which is widely used in cryptography. It's somewhat like protocol
     * buffers but less convenient. This method returns a standard DER
     * encoding of the signature, as recognized by OpenSSL and other
     * libraries.
     */
    fun encodeToDER():ByteArray{
        try {
            return derByteStream().toByteArray()
        }catch (e:Exception){
            throw RuntimeException(e)
        }
    }
    fun decodeFromDER(bytes:ByteArray):ECDSASignature{
        var decoder:ASN1InputStream
        try {
            decoder = ASN1InputStream(bytes)
            val seq:DLSequence = decoder.readObject() as DLSequence
            if(seq == null){
                throw RuntimeException("reached past end of ASN.1 stream.")
            }
            var r:ASN1Integer
            var s:ASN1Integer
            try {
                r = seq.getObjectAt(0) as ASN1Integer
                s = seq.getObjectAt(1) as ASN1Integer
            }catch (e:ClassCastException){
                throw IllegalArgumentException(e)
            }
            decoder.close()
            return ECDSASignature(r.positiveValue,s.positiveValue,this.CURVE,this.HALF_CURVE_ORDER)
        }catch (e:IOException){
            throw RuntimeException(e)
        }finally {
//            if(decoder != null){
//                try {
//                    decoder.close()
//                }catch (e:Exception){
//
//                }
//            }
        }
    }
    fun derByteStream():ByteArrayOutputStream{
        val bos= ByteArrayOutputStream(72)
        val seq = DERSequenceGenerator(bos)
        seq.addObject(ASN1Integer(this.r))
        seq.addObject(ASN1Integer(this.s))
        seq.close()
        return bos
    }

    override fun equals(other: Any?): Boolean {
        if(this == other){
            return true
        }
        if(other == null || this::class != other::class){
            return false
        }
        val o = other as ECDSASignature
        return this.r.equals(o.r) && this.s.equals(o.s)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(this.r,this.s)
    }
}