package com.symbol.eumsung2pop.view.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbol.eumsung2pop.R;
import com.symbol.eumsung2pop.databinding.RowInputBinding;
import com.symbol.eumsung2pop.model.SearchCondition;
import com.symbol.eumsung2pop.model.object.Input;
import com.symbol.eumsung2pop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InputAdapter extends RecyclerView.Adapter<InputAdapter.ViewHolder> implements Filterable {
    Context context;
    private ArrayList<Input> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<Input> unFilteredlist;//for filter
    ArrayList<Input> filteredList;//for filter
    String lastPart;//마지막에 추가된 품목,규격
    public int lastPosition;//마지막에 변화된 행값
    CommonViewModel commonViewModel;

    public InputAdapter(ArrayList<Input> items, Context context,
                           ActivityResultLauncher<Intent> resultLauncher, String lastPart, CommonViewModel commonViewModel) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.lastPart = lastPart;
        this.commonViewModel = commonViewModel;
        //this.barcodeConvertPrintViewModel = barcodeConvertPrintViewModel;
    }


   /* public ProgressFloorReturnViewAdapter(Context context, LinearLayout layoutTop, String contractNo, String fromDate) {
        super();
        this.context = context;
        this.layoutTop = layoutTop;
        this.contractNo = contractNo;
        this.fromDate = fromDate;
    }*/

    public void updateAdapter(ArrayList<Input> newCountries, String lastPart) {
        items.clear();
        items.addAll(newCountries);
        this.lastPart = lastPart;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public InputAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowInputBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_input, viewGroup, false);
        return new InputAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InputAdapter.ViewHolder viewHolder, int position) {
        //StockOut item = items.get(position);
        Input item = filteredList.get(position);//for filter
        viewHolder.setItem(item, position); //왜오류
    }

    @Override
    public int getItemCount() {
        //return items.size();
        return filteredList.size();//for filter
    }


    //for filter
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty() || charString.equals("전체") || charString.equals("ALL")) {
                    filteredList = unFilteredlist;
                } else {
                    ArrayList<Input> filteringList = new ArrayList<>();
                    for (Input input : unFilteredlist) {
                        if (input.PartName2.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(input);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Input>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        RowInputBinding binding;
        //View row;

        public ViewHolder(RowInputBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Input item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.txtTagNo.setText(item.ItemTag);
            binding.txtDate.setText(item.WorkingDate);
            binding.txtPartName.setText(item.PartName);
            binding.txtPartSpec.setText(item.PartSpec);
            binding.txtQty.setText(numFormatter.format(item.ItemCnt));
            binding.txtWeight.setText(numFormatter.format(item.Weight));
            if ((item.ItemTag).equals(lastPart)) {//마지막 변경된 행 강조표시
                binding.layout1.setBackgroundColor(Color.YELLOW);
                binding.layout2.setBackgroundColor(Color.YELLOW);
                binding.layoutQty.setBackgroundColor(Color.YELLOW);
                lastPosition = position;
            }
            else{
                binding.layout1.setBackgroundColor(Color.TRANSPARENT);
                binding.layout2.setBackgroundColor(Color.TRANSPARENT);
                binding.layoutQty.setBackgroundColor(Color.TRANSPARENT);
            }

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (item.StockOutNo.equals(""))
                        return;
                    CommonMethod.FNBarcodeConvertPrint(item.StockOutNo, barcodeConvertPrintViewModel);*/

                    /*SearchCondition sc = new SearchCondition();
                    sc.StockOutNo = stockOutNo;
                    sc.PartCode = item.PartCode;
                    sc.PartSpec = item.PartSpec;
                    commonViewModel.Get4("GetOneItemDataNew", sc);*/

                }
            });

        }
    }

    public void addItem(Input item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<Input> items) {
        this.items = items;
    }

    public Input getItem(int position) {
        return items.get(position);
    }

    public ArrayList<Input> getItemList() {
        return items;
    }

    public void setItem(int position, Input item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}
