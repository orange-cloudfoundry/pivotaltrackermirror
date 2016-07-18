package com.orange.clara.pivotaltrackermirror.repos;

import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 13/07/2016
 */
@Repository
public interface MirrorReferenceRepo extends CrudRepository<MirrorReference, Integer> {
}
