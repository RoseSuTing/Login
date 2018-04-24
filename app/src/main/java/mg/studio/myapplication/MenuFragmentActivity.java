package mg.studio.myapplication;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;


public class MenuFragmentActivity extends Fragment {
    public MenuFragmentActivity() {
        //Required empty constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_menu_fragment, container,false);
        //Handle the click listeners on the buttons
        ImageButton app = view.findViewById(R.id.btn_app);
        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Toast App", Toast.LENGTH_LONG).show();
            }
        });


        //Return the view
        return view;

    }
}
