package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.Property;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropertyRepository extends CrudRepository<Property, UUID> {

    @Query("SELECT * FROM properties WHERE user_id = :userId ORDER BY created_at DESC")
    List<Property> findAllByUserId(@Param("userId") UUID userId);

    @Query("SELECT * FROM properties WHERE user_id = :userId ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    List<Property> findPageByUserId(@Param("userId") UUID userId,
                                    @Param("limit") int limit,
                                    @Param("offset") int offset);

    @Query("SELECT * FROM properties WHERE id = :id AND user_id = :userId")
    Optional<Property> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

    @Query("SELECT * FROM properties WHERE address1 = :address1 AND user_id = :userId")
    Optional<Property> findByAddress1AndUserId(@Param("address1") String address1, @Param("userId") UUID userId);
}
