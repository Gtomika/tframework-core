/**
 * Profiles are a way to control the framework's and the application's behaviour. They are
 * simply strings, that are either set ("active") or not set. Profiles are scanned and set
 * once, at initialization. This process is described at {@link org.tframework.core.profiles.ProfileInitializationProcess}.
 * <br><br>
 * There are several ways to set profiles: see {@link org.tframework.core.profiles.ProfileScanner} implementations. The
 * scanned profiles are combined by {@link org.tframework.core.profiles.ProfileMerger}.
 * <br><br>
 * Some restrictions apply to profile names: see {@link org.tframework.core.profiles.ProfileCleaner} and
 * {@link org.tframework.core.profiles.ProfileValidator}.
 */
@TFrameworkInternal
package org.tframework.core.profiles;

import org.tframework.core.TFrameworkInternal;
