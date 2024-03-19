package org.example.loanbusinessv3.repository.dao;

import java.util.*;

import org.example.loanbusinessv3.model.Profiles;

public interface ProfilesDAO {
    void insertProfile(Profiles profile);

    List<Profiles> getAllProfiles();

    Map<String, Object> selectProfileAndAccount(String email);

    Profiles selectProfile(String email);

    void updateProfile(Map<String, String> parameters, String email);

    void deleteProfile(String email);
}
