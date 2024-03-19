package org.example.loanbusinessv3.repository.dao;

import java.util.List;

import org.example.loanbusinessv3.model.Profiles;

public interface ProfilesDAO {
    void insertProfile(Profiles details);

    List<Profiles> getAllProfiles();

    Profiles selectProfile(String email);

    void updateProfile();

    void deleteProfile();
}
