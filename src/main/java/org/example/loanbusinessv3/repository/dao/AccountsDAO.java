package org.example.loanbusinessv3.repository.dao;

import java.sql.SQLException;
import java.util.List;

import org.example.loanbusinessv3.model.Accounts;

public interface AccountsDAO {
    void insertAccount(Accounts details) throws SQLException;

    List<Accounts> selectAllAccounts();

    Accounts selectAccount(String email);

    void updateAccount(String email, String updatedEmail) throws SQLException;

    void deleteAccount(String email) throws SQLException;
}