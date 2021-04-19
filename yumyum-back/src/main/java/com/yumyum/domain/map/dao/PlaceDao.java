package com.yumyum.domain.map.dao;

import com.yumyum.domain.map.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceDao extends JpaRepository<Place, Long> {

    Boolean existsByAddressAndName(String address, String name);

    List<Place> findByAddressContainingIgnoreCase(String address);

    List<Place> findByNameContainingIgnoreCase(String name);
}
