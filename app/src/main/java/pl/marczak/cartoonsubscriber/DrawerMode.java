package pl.marczak.cartoonsubscriber;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Lukasz Marczak
 * @since 07.06.16.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({Const.LEFT, Const.MIDDLE, Const.RIGHT})
public @interface DrawerMode {
}
