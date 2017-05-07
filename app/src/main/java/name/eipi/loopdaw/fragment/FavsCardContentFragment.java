/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.eipi.loopdaw.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.adapter.CardContentAdapter;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.model.Project;

/**
 * Provides UI for the view with Cards.
 */
public class FavsCardContentFragment extends Fragment {

    private LoopDAWApp app;
    private List<Project> dataList;
    private CardContentAdapter adapter;
    private boolean isFavouritesOnly = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        Context context = recyclerView.getContext();
        app = (LoopDAWApp) context.getApplicationContext();
        dataList = app.getAllProjects();
        adapter = new CardContentAdapter(context, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

//    @Override
//    public void setArguments(final Bundle args) {
//        super.setArguments(args);
//        boolean isFavs = args.getBoolean("isFavouritesOnly", false);
//        if (isFavs) {
//            adapter
//        }
//        adapter.notifyDataSetChanged();
//    }

}
