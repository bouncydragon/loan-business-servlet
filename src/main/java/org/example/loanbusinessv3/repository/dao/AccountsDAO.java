package org.example.loanbusinessv3.repository.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Profiles;

public interface AccountsDAO {
    Accounts createAccount(Accounts account) throws SQLException;

    Profiles createProfile(Profiles profile) throws SQLException;

    List<Accounts> findAllWithProfiles();

    Accounts findById(Long accountId);

    Map<String, Object> findByEmailWithProfile(String email);

    void removeAccount(String email) throws SQLException;
}
