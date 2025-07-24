package com.symbol.eumsung2pop.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbol.eumsung2pop.R;
import com.symbol.eumsung2pop.databinding.RowMenuBinding;
import com.symbol.eumsung2pop.model.object.MainMenuItem;
import com.symbol.eumsung2pop.view.activity.InputActivity;
import com.symbol.eumsung2pop.view.activity.InventorySurveyActivity;
import com.symbol.eumsung2pop.view.activity.MainActivity;
/*import com.symbol.eumsung2pop.view.activity.menu0.Activity0000;
import com.symbol.eumsung2pop.view.activity.menu1.Activity1000;
import com.symbol.eumsung2pop.view.activity.menu2.Activity2000;
import com.symbol.eumsung2pop.view.activity.menu3.Activity3000;
import com.symbol.eumsung2pop.view.activity.menu4.Activity4000;
import com.symbol.eumsung2pop.view.activity.menu5.Activity5000;
import com.symbol.eumsung2pop.view.activity.menu9.Activity9000;
import com.symbol.eumsung2pop.view.activity.rawmaterial.CarryOutRawMaterialActivity;
import com.symbol.eumsung2pop.view.activity.rawmaterial.TransferRawMaterialActivity;
import com.symbol.eumsung2pop.view.activity.report.ActivityReport1;
import com.symbol.eumsung2pop.view.activity.report.ActivityReport2;
import com.symbol.eumsung2pop.view.activity.report.ActivityReport3;
import com.symbol.eumsung2pop.view.activity.report.ActivityReport4;
import com.symbol.eumsung2pop.view.activity.report.ActivityReport5;
import com.symbol.eumsung2pop.view.activity.report.ActivityReport6;
import com.symbol.eumsung2pop.view.activity.report.ActivityReport7;*/

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<MainMenuItem> items = new ArrayList<>();
    boolean confirmFlag;

    public MainAdapter(ArrayList<MainMenuItem> items, Context context) {
        this.context = context;
        this.items = items;
    }


   /* public ProgressFloorReturnViewAdapter(Context context, LinearLayout layoutTop, String contractNo, String fromDate) {
        super();
        this.context = context;
        this.layoutTop = layoutTop;
        this.contractNo = contractNo;
        this.fromDate = fromDate;
    }*/

    public void updateAdapter(ArrayList<MainMenuItem> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowMenuBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_menu, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MainMenuItem item = items.get(position);
        viewHolder.setItem(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        RowMenuBinding binding;
        //View row;

        public ViewHolder(RowMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(MainMenuItem item, int position) {

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            if (item.menuType == 1) {//각 그룹의 첫항목 이미지 없애기, 글자 크게
                binding.imvIcon.setVisibility(View.GONE);
                binding.txtMenuName.setTextSize(19);
                binding.txtMenuName.setTypeface(null, Typeface.BOLD); //only text style(only bold)


                DisplayMetrics dm = context.getResources().getDisplayMetrics();

                param.leftMargin = (int) (15 * dm.density);
                binding.txtMenuName.setLayoutParams(param);
                binding.getRoot().setEnabled(false);
            } else {
                binding.imvIcon.setVisibility(View.VISIBLE);
                //binding.imvIcon.setImageDrawable(context.getResources().getDrawable(item.imageID));
                binding.imvIcon.setImageDrawable(context.getDrawable(item.imageID));
                binding.txtMenuName.setTextSize(17);
                binding.txtMenuName.setTypeface(null, Typeface.NORMAL); //only text style(only bold)

                param.leftMargin = 0;
                binding.txtMenuName.setLayoutParams(param);
                binding.getRoot().setEnabled(true);
            }
            FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (item.lastItem) {//그릅의 마지막 항목 줄 띄우기

                param2.bottomMargin = 40;
            } else {
                param2.bottomMargin = 0;
            }
            binding.cardView.setLayoutParams(param);
           /* if(item.menuType==1){//선택불가
                binding.getRoot().setEnabled(false);
            }
            else{
                binding.getRoot().setEnabled(true);
            }*/
            binding.txtMenuName.setText(item.menuName);
            //binding.getRoot().setOn

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (item.menuName.equals(context.getString(R.string.menu1)) ||
                            item.menuName.equals(context.getString(R.string.menu1_eng))) {
                        ((MainActivity) (context)).LoadScannerOrKeyboard(1);//TAG수정:1
                    } else if (item.menuName.equals(context.getString(R.string.menu2)) ||
                            item.menuName.equals(context.getString(R.string.menu2_eng))) {
                        ((MainActivity) (context)).LoadScannerOrKeyboard(2);//출고등록:2
                    }else if (item.menuName.equals(context.getString(R.string.menu3)) ||
                            item.menuName.equals(context.getString(R.string.menu3_eng))) {
                        Intent intent = new Intent(context, InputActivity.class);
                        context.startActivity(intent);
                    }
                    else if (item.menuName.equals(context.getString(R.string.menu4)) ||
                            item.menuName.equals(context.getString(R.string.menu4_eng))) {
                        ((MainActivity) (context)).LoadScannerOrKeyboard(4);//포장등록:4
                    }

                    else if (item.menuName.equals(context.getString(R.string.menu5)) ||
                            item.menuName.equals(context.getString(R.string.menu5_eng))) {
                        Intent intent = new Intent(context, InventorySurveyActivity.class);
                        context.startActivity(intent);
                    }

                    /*if (item.menuName.equals(context.getString(R.string.menu2)) ||
                            item.menuName.equals(context.getString(R.string.menu2_eng))) {
                        Intent intent = new Intent(context, Activity9000.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu3)) ||
                            item.menuName.equals(context.getString(R.string.menu3_eng))) {
                        Intent intent = new Intent(context, Activity1000.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu4)) ||
                            item.menuName.equals(context.getString(R.string.menu4_eng))) {

                    } else if (item.menuName.equals(context.getString(R.string.menu5)) ||
                            item.menuName.equals(context.getString(R.string.menu5_eng))) {
                        Intent intent = new Intent(context, ActivityReport1.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu6)) ||
                            item.menuName.equals(context.getString(R.string.menu6_eng))) {
                        Intent intent = new Intent(context, ActivityReport2.class);
                        context.startActivity(intent);

                    } else if (item.menuName.equals(context.getString(R.string.menu7)) ||
                            item.menuName.equals(context.getString(R.string.menu7_eng))) {
                        Intent intent = new Intent(context, ActivityReport3.class);
                        context.startActivity(intent);

                    } else if (item.menuName.equals(context.getString(R.string.menu8)) ||
                            item.menuName.equals(context.getString(R.string.menu8_eng))) {
                        Intent intent = new Intent(context, ActivityReport4.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu9)) ||
                            item.menuName.equals(context.getString(R.string.menu9_eng))) {
                        Intent intent = new Intent(context, ActivityReport5.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu16)) ||
                            item.menuName.equals(context.getString(R.string.menu16_eng))) {
                        Intent intent = new Intent(context, ActivityReport6.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu17)) ||
                            item.menuName.equals(context.getString(R.string.menu17_eng))) {
                        Intent intent = new Intent(context, ActivityReport7.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu11)) ||
                            item.menuName.equals(context.getString(R.string.menu11_eng))) {//제품포장
                        Intent intent = new Intent(context, Activity0000.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu12)) ||
                            item.menuName.equals(context.getString(R.string.menu12_eng))) {//번들생성
                        Intent intent = new Intent(context, Activity2000.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu13)) ||
                            item.menuName.equals(context.getString(R.string.menu13_eng))) {//재고이송
                        Intent intent = new Intent(context, Activity3000.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu14)) ||
                            item.menuName.equals(context.getString(R.string.menu14_eng))) {//제품출고
                        Intent intent = new Intent(context, Activity5000.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu15)) ||
                            item.menuName.equals(context.getString(R.string.menu15_eng))) {//자료수신
                        *//*Intent intent = new Intent(context, Activity0000.class);
                        context.startActivity(intent);*//*
                    } else if (item.menuName.equals(context.getString(R.string.menu18)) ||
                            item.menuName.equals(context.getString(R.string.menu18_eng))) {//재고이입
                        Intent intent = new Intent(context, Activity4000.class);
                        context.startActivity(intent);
                    } else if (item.menuName.equals(context.getString(R.string.menu19)) ||
                            item.menuName.equals(context.getString(R.string.menu19_eng))) {//출고검수
                        *//*Intent intent = new Intent(context, Activity0000.class);
                        context.startActivity(intent);*//*
                    } else if (item.menuName.equals(context.getString(R.string.menu20)) ||
                            item.menuName.equals(context.getString(R.string.menu20_eng))) {//자료송신
                        *//*Intent intent = new Intent(context, Activity0000.class);
                        context.startActivity(intent);*//*
                    }
                    else if (item.menuName.equals(context.getString(R.string.menu22)) ||
                            item.menuName.equals(context.getString(R.string.menu22_eng))) {//원자재불출
                        Intent intent = new Intent(context, CarryOutRawMaterialActivity.class);
                        context.startActivity(intent);
                    }
                    else if (item.menuName.equals(context.getString(R.string.menu23)) ||
                            item.menuName.equals(context.getString(R.string.menu23_eng))) {//원자재이동
                        Intent intent = new Intent(context, TransferRawMaterialActivity.class);
                        context.startActivity(intent);
                    }*/
                }
            });
        }
    }

    public void removeItem(int position) {
        items.remove(position);
    }


    public void addItem(MainMenuItem item) {
        items.add(item);
    }

    public void setItems(ArrayList<MainMenuItem> items) {
        this.items = items;
    }

    public MainMenuItem getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, MainMenuItem item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}


