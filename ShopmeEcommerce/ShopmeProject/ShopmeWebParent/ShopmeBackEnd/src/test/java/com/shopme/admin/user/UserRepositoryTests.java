package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userAyman = new User("ayman@gmail.com", "1234", "ayman", "abusafia");
		userAyman.addRole(roleAdmin);

		User savedUser = userRepo.save(userAyman);
		assertThat(savedUser.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateNewUserWithTwoRole() {
		User userAhmad = new User("ahmad@gmail.com", "12345", "ahmad", "abusafia");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

		userAhmad.addRole(roleEditor);
		userAhmad.addRole(roleAssistant);

		User savedUser = userRepo.save(userAhmad);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = userRepo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById() {
		User userAyman = userRepo.findById(1).get();
		System.out.println(userAyman);
		assertThat(userAyman).isNotNull();
	}

	@Test
	public void testUpdateUserDetails() {
		User userAyman = userRepo.findById(1).get();
		userAyman.setEnabled(true);
		userAyman.setEmail("aymanabusafia@gmail.com");
		userRepo.save(userAyman);
	}

	@Test
	public void testUpdateUserRoles() {
		User userAhmad = userRepo.findById(8).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);

		userAhmad.getRoles().remove(roleEditor);
		userAhmad.addRole(roleSalesperson);

		userRepo.save(userAhmad);
	}

	@Test
	public void testDeleteUser() {
		Integer userId = 8;
		userRepo.deleteById(userId);

	}

	@Test
	public void testGetUserByEmail() {
		String email = "ahmad@gmail.com";
		User user = userRepo.getUserByEmail(email);
		assertThat(user).isNotNull();
	}

	@Test
	public void testCountById() {
		Integer id = 4;
		Long countById = userRepo.countById(id);

		assertThat(countById).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testDisableUser() {
		Integer id = 4;
		userRepo.updateEnabledStatus(id, false);

	}

	@Test
	public void testEnabledUser() {
		Integer id = 9;
		userRepo.updateEnabledStatus(id, true);
	}

	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepo.findAll(pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isEqualTo(pageSize);

	}

	@Test
	public void testSearchUsers() {
		String keyword = "bruce";

		int pageNumber = 0;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepo.findAll(keyword, pageable);
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isGreaterThan(0);
	}

}
