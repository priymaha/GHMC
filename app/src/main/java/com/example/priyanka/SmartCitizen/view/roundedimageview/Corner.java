/**
 * Copyright (C) 2016 KeepTrax, Inc. All Rights Reserved.
 * <p>
 * NOTICE:  All information contained herein is, and remains the property of
 * KeepTrax Incorporated.  The intellectual and technical concepts contained
 * herein are proprietary to KeepTrax Incorporated and may be covered by U.S.
 * and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this
 * material as a whole or in part is strictly forbidden unless prior written
 * permission is obtained from KeepTrax Incorporated.
 */
package com.example.priyanka.SmartCitizen.view.roundedimageview;

/**
 * Created by madhuri on 4/4/16.
 */

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        Corner.TOP_LEFT, Corner.TOP_RIGHT,
        Corner.BOTTOM_LEFT, Corner.BOTTOM_RIGHT
})
public @interface Corner {
    int TOP_LEFT = 0;
    int TOP_RIGHT = 1;
    int BOTTOM_RIGHT = 2;
    int BOTTOM_LEFT = 3;
}
