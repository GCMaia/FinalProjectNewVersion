package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gabri.finalprojectnewversion.R;

public class OCTranspoFragment extends android.app.Fragment {
    private TextView messageContent;
    private TextView messageID;
    private Button deleteButton;
    private Bundle runningBundle;
    private Context parent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runningBundle = this.getArguments();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.activity_octranspo_fragment,container,false);
        messageContent = result.findViewById(R.id.OCTranspoDestination);
        messageID = result.findViewById(R.id.OCTranspoLatitudeLongitude);
        deleteButton = result.findViewById(R.id.OCTranspoSaveButton);
        String ID = Long.toString(runningBundle.getLong("ID"));
        messageID.setText(ID);
        String msg = runningBundle.getString("message");
        messageContent.setText(msg);
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        runningBundle = this.getArguments();
        parent = context;
    }
}
