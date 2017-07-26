package com.mede.zepan;

import com.mede.zepan.configs.PostConfig;

/**
 *
 */
public interface PosterFactory {

    
    Poster newPoster(PostConfig config);
}
