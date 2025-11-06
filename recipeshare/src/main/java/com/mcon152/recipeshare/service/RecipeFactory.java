package com.mcon152.recipeshare.service;

import com.mcon152.recipeshare.domain.Recipe;
import com.mcon152.recipeshare.domain.RecipeRegistry;
import com.mcon152.recipeshare.web.RecipeRequest;

/**
 * @deprecated Use {@link com.mcon152.recipeshare.domain.RecipeRegistry#createFromRequest} directly.
 * This class remains only as a deprecated shim and will be removed in a future release.
 */
@Deprecated
public final class RecipeFactory {

    private RecipeFactory() { /* prevent instantiation */ }

    public static Recipe createFromRequest(RecipeRequest req) {
        // Delegate to the new registry for backward compatibility.
        return RecipeRegistry.createFromRequest(req);
    }
}
