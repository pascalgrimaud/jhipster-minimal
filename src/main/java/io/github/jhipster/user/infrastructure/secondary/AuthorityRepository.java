package io.github.jhipster.user.infrastructure.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link AuthorityEntity} entity.
 */
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, String> {}
