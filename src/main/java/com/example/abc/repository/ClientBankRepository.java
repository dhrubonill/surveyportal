package com.example.abc.repository;

import com.example.abc.entity.ClientBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientBankRepository extends JpaRepository<ClientBank,Long> {
}
