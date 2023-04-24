package com.gina.takecare4u.activities.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gina.takecare4u.R;
import com.gina.takecare4u.activities.FilterActivity;
import com.gina.takecare4u.adapter.PubliAdapter;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.PublicacionProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment {

    View mViewFilter;
    CardView mCardViewOld;
    CardView mCardViewChild;
    CardView mCardViewPet;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       mViewFilter= inflater.inflate(R.layout.fragment_filter, container, false);
       mCardViewOld =    mViewFilter.findViewById(R.id.cardViewOldFilerCard);
       mCardViewChild = mViewFilter.findViewById(R.id.cardViewChildCard);
       mCardViewPet=    mViewFilter.findViewById(R.id.cardViewPetCard);

       mCardViewOld.setOnClickListener(v -> goToFilterActivity("ADULTOS MAYORES"));
       mCardViewChild.setOnClickListener(v -> goToFilterActivity("NIÑOS"));
       mCardViewPet.setOnClickListener(v -> goToFilterActivity("MASCOTAS"));

        return mViewFilter;
    }

    private void goToFilterActivity(String servicio){
        Intent intent = new Intent(getContext(), FilterActivity.class);
        intent.putExtra("Servicio", servicio);
        startActivity(intent);


    }
}