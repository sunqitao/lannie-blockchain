package cn.lannie.kt.blockchain.core.sqlite.config.identiry

import cn.hutool.db.dialect.Dialect
import org.hibernate.dialect.identity.IdentityColumnSupportImpl


/**
 */
class SQLiteDialectIdentityColumnSupport(dialect: org.hibernate.dialect.Dialect) : IdentityColumnSupportImpl() {

    override fun supportsIdentityColumns(): Boolean {
        return true
    }

    /*
	public boolean supportsInsertSelectIdentity() {
    return true; // As specified in NHibernate dialect
  }
  */

    override fun hasDataTypeInIdentityColumn(): Boolean {
        // As specified in NHibernate dialect
        // FIXME true
        return false
    }

    /*
	public String appendIdentitySelectToInsert(String insertString) {
    return new StringBuffer(insertString.length()+30). // As specified in NHibernate dialect
      append(insertString).
      append("; ").append(getIdentitySelectString()).
      toString();
  }
  */

    override fun getIdentitySelectString(table: String?, column: String?, type: Int): String {
        return "select last_insert_rowid()"
    }

    override fun getIdentityColumnString(type: Int): String {
        // return "integer primary key autoincrement";
        // FIXME "autoincrement"
        return "integer"
    }
}