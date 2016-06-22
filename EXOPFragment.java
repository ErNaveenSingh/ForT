package com.tcs.iwms.TrackApplication;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tcs.iwms.R;
import com.tcs.iwms.connection.ConnectionURLConstants;
import com.tcs.iwms.worklist.SimpleDividerItemDecoration;
import com.tcs.iwms.worklistVO.WorkListRequestListVO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EXOPFragment extends Fragment {

    private RecyclerView exopListRecycler;
    private TextView emptyView;
    private EditText exopTrackSearch;
    private ArrayList<TrackApplicationBean> trackEXOPParentArray;
    private EXOPListRecyclerAdapter exopListRecyclerAdapter;
    private ArrayList<TrackApplicationBean> trackEXOPChildArray;
    boolean searchMode = false;


    public EXOPFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("track exop", "req insdie EXOp fragmnt");
        //return inflater.inflate(R.layout.fragment_supervisor_child, container, false);
        View view =null;
        view = inflater.inflate(R.layout.fragment_supervisor_child, container, false);

        emptyView = (TextView) view.findViewById(R.id.empty_view);
        exopTrackSearch = (EditText) view.findViewById(R.id.worklist_searchbar);
        exopListRecycler = (RecyclerView) view.findViewById(R.id.worklist_recycler_view);

        exopTrackSearch.setHint("Enter Request ID or Role to search");

        Bundle bundle = getArguments();
        if (bundle != null) {

            trackEXOPParentArray = bundle.getParcelableArrayList("EXOP_DATA_LIST");
            if (trackEXOPParentArray!=null)
            {
                Log.d("track exop", "track EXOPdata parent found :)" + trackEXOPParentArray.size());

                exopListRecycler.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                exopListRecycler.setLayoutManager(llm);
                exopListRecycler.addItemDecoration(new SimpleDividerItemDecoration(getResources()));

                populateEXOPDataAdapter(trackEXOPParentArray);

            }else
            {
                Log.d("track exop", "parent EXOPnot found :)");
            }

        } else {
            Log.d("track exop", "data EXOP not present :(");
        }

        //text change listener in search bar
        exopTrackSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable query) {

                String regexStr = "^[0-9]*$";

                //search mode to differentiate b/w parent n child dataset tobe used
                if (query.toString().length()!=0)
                {
                    searchMode =true;
                }else
                {
                    searchMode =false;
                }

                if (trackEXOPParentArray!=null)
                {

                    if (query.toString().trim().matches(regexStr)) {
                        //numeric data
                        trackEXOPChildArray = exopFilterRequestId(trackEXOPParentArray, query.toString());
                    } else {
                        // non numeric data
                        trackEXOPChildArray = exopFilterEmpRole(trackEXOPParentArray, query.toString());
                    }


                    if (trackEXOPChildArray!=null && exopListRecyclerAdapter!=null)
                    {
                        if (trackEXOPChildArray.size()!=0)
                        {
                            emptyView.setVisibility(View.GONE);
                            exopListRecyclerAdapter.animateTo(trackEXOPChildArray);
                            exopListRecycler.scrollToPosition(0);
                        }else
                        {
                            exopListRecyclerAdapter.animateTo(trackEXOPChildArray);
                            exopListRecycler.scrollToPosition(0);
                            emptyView.setVisibility(View.VISIBLE);
                            emptyView.setText(ConnectionURLConstants.NOSEARCHFOUND);
                        }

                    }

                }else
                {
                    //data list is empty
                    exopTrackSearch.setEnabled(false);
                    exopListRecycler.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(ConnectionURLConstants.NODATAFOUND);

                }

            }
        });

        return view;
    }

    private void populateEXOPDataAdapter(final ArrayList<TrackApplicationBean> EXOPParentArray)
    {
        Log.d("track", "exop parent fragment EXOPParentArray"+EXOPParentArray.size());
        exopListRecyclerAdapter = new EXOPListRecyclerAdapter(getActivity(), trackEXOPParentArray);

        exopListRecyclerAdapter.notifyDataSetChanged();

        exopListRecycler.setItemAnimator(new DefaultItemAnimator());
        exopListRecycler.setAdapter(exopListRecyclerAdapter);

        exopListRecyclerAdapter.SetOnItemClickListner(new EXOPListRecyclerAdapter.OnItemClickInterface() {
            @Override
            public void onItemClick(View view, int position) {

                Log.d("track", "exop parent fragment onClick");

                Log.d("track", "exop onclick value" + EXOPParentArray.get(position).getRqstId());

               /* if (!searchMode)
                {

                }else {

                }*/
            }
        });

    }




    //filter role name
    private ArrayList<TrackApplicationBean> exopFilterEmpRole(ArrayList<TrackApplicationBean> models, String query) {
        query = query.toLowerCase();

        final ArrayList<TrackApplicationBean> filteredModelList = new ArrayList<TrackApplicationBean>();
        for (TrackApplicationBean model : models) {
            //final String text = Integer.toString(model.getRqstId());
            if (model.getRoleName()!=null)
            {
                final String text = model.getRoleName().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }

    //filter Request ID
    private ArrayList<TrackApplicationBean> exopFilterRequestId(ArrayList<TrackApplicationBean> models, String query) {

        final ArrayList<TrackApplicationBean> filteredModeldata = new ArrayList<TrackApplicationBean>();
        for (TrackApplicationBean model : models) {
                String text = String.valueOf(model.getRqstId());
            if (text.contains(query)) {
                filteredModeldata.add(model);
            }
        }
        return filteredModeldata;
    }

    public void setExopFilteredData(ArrayList<TrackApplicationBean> trackEXOPFilteredArray)
    {
        Log.d("track", "setExopFilteredData -tAb fragment - EXOP " +trackEXOPFilteredArray.size());

        trackEXOPParentArray=trackEXOPFilteredArray;
        Log.d("track","set filter data in exoparraylist"+trackEXOPParentArray.size());
        exopListRecyclerAdapter.notifyDataSetChanged();
        exopListRecycler.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("track","exop onresume");
    }
}
