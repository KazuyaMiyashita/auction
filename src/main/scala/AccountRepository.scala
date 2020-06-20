trait AccountRepository {
  def fetchBy(accountId: Int): Option[Account]
  def list(): List[Account]
  def store(account: Account): Boolean
}

class AccountRepositoryInMemory extends AccountRepository {

  // AccountId, Account
  private val accounts: collection.mutable.HashMap[Int, Account] = 
    collection.mutable.HashMap[Int, Account]()

  override def fetchBy(accountId: Int): Option[Account] = {
    accounts.get(accountId)
  }

  override def list(): List[Account] = accounts.values.toList

  override def store(account: Account): Boolean = {
    accounts.put(account.id, account).isDefined
  }


}
