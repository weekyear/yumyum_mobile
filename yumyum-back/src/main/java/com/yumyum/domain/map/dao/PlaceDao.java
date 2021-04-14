package com.yumyum.domain.map.dao;

import com.yumyum.domain.map.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceDao extends JpaRepository<Place, Long> {

}
