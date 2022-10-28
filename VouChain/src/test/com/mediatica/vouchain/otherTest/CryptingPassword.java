package test.com.mediatica.vouchain.otherTest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CryptingPassword {

	@Test
	public void test() {
		String plain_password="ciao2";
//		String salt = BCrypt.gensalt();
		String strong_salt = BCrypt.gensalt(10);
//		String stronger_salt = BCrypt.gensalt(12);
		

		String pw_hash = BCrypt.hashpw(plain_password, strong_salt);
		System.out.println("plain_password is: "+plain_password);
		System.out.println("pw_hash is		 : "+pw_hash);
		System.out.println("salt is			 : "+strong_salt);
		plain_password ="ciao3";
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // Strength set as 12
//		String encodedPassword = encoder.encode("UserPassword");
//		System.out.println("BCryptPasswordEncoder pw_hash is: "+encodedPassword);
		try {
			System.out.println("is the same:"+BCrypt.checkpw(plain_password, pw_hash));
		}catch(Exception e){
		e.printStackTrace();
	}
				fail("Not yet implemented");
	}

}
