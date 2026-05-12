package com.Croquetas.repository;

	import com.Croquetas.model.User;
	import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

	public interface UserRepository extends JpaRepository<User, Long> {
	    Optional<User> findByUsername(String username);
	    @Query("SELECT u FROM User u WHERE u.approved = false")
	    List<User> findByApprovedFalse();
	    @Query(value = "SELECT * FROM users WHERE approved = false", nativeQuery = true)
	    List<User> findPendingUsers();
	    @Query("SELECT DISTINCT r.chef FROM Recipe r WHERE r.isPublic = true ORDER BY r.chef.fullName")
	    List<User> findChefsWithPublicRecipes();
	
}
