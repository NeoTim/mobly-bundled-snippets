/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.android.mobly.snippet.bundled;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.android.mobly.snippet.Snippet;
import com.google.android.mobly.snippet.rpc.Rpc;

public class BluetoothAdapterSnippet implements Snippet {
    private static class BluetoothException extends Exception {
        public BluetoothException(String msg) {
            super(msg);
        }
    }

    private final Context mContext;
    private final BluetoothAdapter mBluetoothAdapter;

    public BluetoothAdapterSnippet() {
        mContext = InstrumentationRegistry.getContext();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Rpc(description = "Enable bluetooth")
    public void bluetoothEnable() throws BluetoothException, InterruptedException {
        if (!mBluetoothAdapter.enable()) {
            throw new BluetoothException("Failed to start enabling bluetooth");
        }
        int timeout = 30;
        while (!mBluetoothAdapter.isEnabled() && timeout >= 0) {
            Thread.sleep(1000);
            timeout -= 1;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            throw new BluetoothException("Bluetooth did not turn on before timeout");
        }
    }

    @Rpc(description = "Disable bluetooth")
    public void bluetoothDisable() throws BluetoothException, InterruptedException {
        if (!mBluetoothAdapter.disable()) {
            throw new BluetoothException("Failed to start disabling bluetooth");
        }
        int timeout = 30;
        while (mBluetoothAdapter.isEnabled() && timeout >= 0) {
            Thread.sleep(1000);
            timeout -= 1;
        }
        if (mBluetoothAdapter.isEnabled()) {
            throw new BluetoothException("Bluetooth did not turn off before timeout");
        }
    }

    @Override
    public void shutdown() {
    }
}
