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
import com.symbol.eumsung2pop.databinding.RowStockoutBinding;
import com.symbol.eumsung2pop.model.SearchCondition;
import com.symbol.eumsung2pop.model.object.StockOutDetail;
import com.symbol.eumsung2pop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StockOutAdapter extends RecyclerView.Adapter<StockOutAdapter.ViewHolder>  implements Filterable{

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<StockOutDetail> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<StockOutDetail> unFilteredlist;//for filter
    ArrayList<StockOutDetail> filteredList;//for filter
    String lastPart;//마지막에 추가된 품목,규격
    public int lastPosition;//마지막에 변화된 행값
    String stockOutNo;
    CommonViewModel commonViewModel;

    public StockOutAdapter(ArrayList<StockOutDetail> items, Context context,
                           ActivityResultLauncher<Intent> resultLauncher, String lastPart, String stockOutNo, CommonViewModel commonViewModel) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.lastPart = lastPart;
        this.stockOutNo = stockOutNo;
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

    public void updateAdapter(ArrayList<StockOutDetail> newCountries, String lastPart) {
        items.clear();
        items.addAll(newCountries);
        this.lastPart = lastPart;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowStockoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_stockout, viewGroup, false);
        return new StockOutAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //StockOut item = items.get(position);
        StockOutDetail item = filteredList.get(position);//for filter
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
                if (charString.isEmpty()) {
                    filteredList = unFilteredlist;
                } else {
                    ArrayList<StockOutDetail> filteringList = new ArrayList<>();
                    for (StockOutDetail stockOutDetail : unFilteredlist) {
                        /*if (stockOutDetail.StockOutNo.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(stockOutDetail);
                        }*/
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<StockOutDetail>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        RowStockoutBinding binding;
        //View row;

        public ViewHolder(RowStockoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(StockOutDetail item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.textViewPartName.setText(item.PartName);
            binding.textViewPartSpecName.setText(item.PartSpecName);
            binding.textViewEA.setText(item.ScanQty);//출고
            binding.textViewEA2.setText(item.OutQty);//지시

            if ((item.PartCode + "-" + item.PartSpec).equals(lastPart)) {//마지막 변경된 행 강조표시
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

                    SearchCondition sc = new SearchCondition();
                    sc.StockOutNo = stockOutNo;
                    sc.PartCode = item.PartCode;
                    sc.PartSpec = item.PartSpec;
                    commonViewModel.Get4("GetOneItemDataNew", sc);

                }
            });

        }
    }

    public void addItem(StockOutDetail item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<StockOutDetail> items) {
        this.items = items;
    }

    public StockOutDetail getItem(int position) {
        return items.get(position);
    }

    public ArrayList<StockOutDetail> getItemList() {
        return items;
    }

    public void setItem(int position, StockOutDetail item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}
