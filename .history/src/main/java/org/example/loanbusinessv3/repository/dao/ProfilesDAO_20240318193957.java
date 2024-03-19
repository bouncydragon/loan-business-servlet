package org.example.loanbusinessv3.repository.dao;

import java.util.*;

import org.example.loanbusinessv3.model.Profiles;

public interface ProfilesDAO {
    void insertProfile(Profiles details);

    List<Profiles> getAllProfiles();

    Map<String, Object> selectProfile(String email);

    void updateProfile();

    void deleteProfile();
}
