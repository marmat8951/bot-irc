package data;

public class TestDAO {

	public TestDAO() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		ISPDAO dao = ISPDAO.getInstance();
		ISP i = dao.getISP(1);
		System.out.println(i);
		

	}

}
