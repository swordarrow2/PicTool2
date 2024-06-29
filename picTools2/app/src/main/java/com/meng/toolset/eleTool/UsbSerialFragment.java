package com.meng.toolset.eleTool;

import android.content.*;
import android.hardware.usb.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.hoho.android.usbserial.driver.*;
import com.meng.app.BaseFragment;
import com.meng.toolset.mediatool.*;
import com.meng.tools.MaterialDesign.*;

import java.util.*;
import java.io.*;

public class UsbSerialFragment extends BaseFragment {

    /*
     *@author 清梦
     *@date 2024-06-12 16:47:46
     */
    public static final String TAG = "UsbSerialFragment";

    private ListView list;
    private FloatingButton fb;
    private ArrayAdapter ladpt;
    private ArrayList<String> devices = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.list_fragment, null);
        list = (ListView) inflate.findViewById(R.id.list);
        fb = (FloatingButton) inflate.findViewById(R.id.fab_add);
        ladpt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, devices);
        list.setAdapter(ladpt);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            process();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void process() throws IOException {
        // Find all available drivers from attached devices.
        UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return;
        }
        devices.add(availableDrivers.toString());

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
            return;
        }

        UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        port.open(connection);
        port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

    /*    
        usbIoManager = new SerialInputOutputManager(usbSerialPort, this);
        usbIoManager.start();
        ...
        port.write("hello".getBytes(), WRITE_WAIT_MILLIS);

    @Override
    public void onNewData(byte[] data) {
        runOnUiThread(() -> { textView.append(new String(data)); });
    }
        */
    }

}
