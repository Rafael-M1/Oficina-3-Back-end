package br.edu.ifmt.cba.ifmthub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.Role;
import br.edu.ifmt.cba.ifmthub.model.Tag;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByAuthority(String authority);
}
