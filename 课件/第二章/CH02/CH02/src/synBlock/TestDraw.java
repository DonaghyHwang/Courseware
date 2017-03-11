package synBlock;
public class TestDraw
{
    public static void main(String[] args) 
    {
		//创建一个账户
		Account acct = new Account("1234567" , 1000);
		//模拟两个线程对同一个账户取钱
		new DrawThread("甲" , acct , 800).start();
		new DrawThread("乙" , acct , 800).start();
    }
}
 
class Account
{
	private String accountNo;
	private double balance;


	public Account(){}

	public Account(String accountNo , double balance)
	{
		this.accountNo = accountNo;
		this.balance = balance;
	}

	public void setAccountNo(String accountNo)
	{
		this.accountNo = accountNo;
	}
	public String getAccountNo()
	{
		 return this.accountNo;
	}

	public void setBalance(double balance)
	{
		this.balance = balance;
	}
	public double getBalance()
	{
		 return this.balance;
	}

	public int hashCode()
	{
		return accountNo.hashCode();
	}
	public boolean equals(Object obj)
	{
		if (obj != null && obj.getClass() == Account.class)
		{
			Account target = (Account)obj;
			return target.getAccountNo().equals(accountNo);
		}
		return false;
	}
}

 class DrawThread extends Thread
{
	//模拟用户账户
	private Account account;
	//当前取钱线程所希望取的钱数
	private double drawAmount;

	public DrawThread(String name , Account account , 
		double drawAmount)
	{
		super(name);
		this.account = account;
		this.drawAmount = drawAmount;
	}

	//当多条线程修改同一个共享数据时，将涉及到数据安全问题。
	public void run()
	{
		//使用account作为同步监视器，任何线程进入下面同步代码块之前，
		//必须先获得对account账户的锁定——其他线程无法获得锁，也就无法修改它
		//这种做法符合：加锁-－>修改完成-->释放锁 逻辑
		synchronized (account)
		{
			//账户余额大于取钱数目
			if (account.getBalance() >= drawAmount)
			{
				//吐出钞票
				System.out.println(getName() + 
					"取钱成功！吐出钞票:" + drawAmount);
				try
				{
					Thread.sleep(1);			
				}
				catch (InterruptedException ex)
				{
					ex.printStackTrace();
				}
				//修改余额
				account.setBalance(account.getBalance() - drawAmount);
				System.out.println("\t余额为: " + account.getBalance());
			}
			else
			{
				System.out.println(getName() + "取钱失败！余额不足！");
			}
		}
	}
}