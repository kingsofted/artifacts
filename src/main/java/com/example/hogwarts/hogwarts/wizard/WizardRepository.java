package com.example.hogwarts.hogwarts.wizard;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WizardRepository extends JpaRepository<Wizard, Integer>{
    boolean existsByName(String name);

    Optional<Wizard> findByName(String name);
}
