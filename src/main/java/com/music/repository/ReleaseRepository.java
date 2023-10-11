package com.music.repository;

import com.music.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReleaseRepository extends JpaRepository<Release, UUID> {}
