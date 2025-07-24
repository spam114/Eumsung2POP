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
import com.symbol.eumsung2pop.databinding.RowPackingBinding;
import com.symbol.eumsung2pop.model.SearchCondition;
import com.symbol.eumsung2pop.model.object.Packing;
import com.symbol.eumsung2pop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PackingAdapter extends RecyclerView.Adapter<PackingAdapter.ViewHolder>  implements Filterable{

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<Packing> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<Packing> unFilteredlist;//for filter
    ArrayList<Packing> filteredList;//for filter
    String lastPart;//마지막에 추가된 품목,규격
    public int lastPosition;//마지막에 변화된 행값
    String stockCommitNo;
    CommonViewModel commonViewModel;

    public PackingAdapter(ArrayList<Packing> items, Context context,
                           ActivityResultLauncher<Intent> resultLauncher, String lastPart, String stockCommitNo, CommonViewModel commonViewModel) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.lastPart = lastPart;
        this.stockCommitNo = stockCommitNo;
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

    public void updateAdapter(ArrayList<Packing> newCountries, String lastPart) {
        items.clear();
        items.addAll(newCountries);
        this.lastPart = lastPart;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowPackingBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_packing, viewGroup, false);
        return new PackingAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //StockOut item = items.get(position);
        Packing item = filteredList.get(position);//for filter
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
                    ArrayList<Packing> filteringList = new ArrayList<>();
                    for (Packing stockOutDetail : unFilteredlist) {
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
                filteredList = (ArrayList<Packing>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        RowPackingBinding binding;
        //View row;

        public ViewHolder(RowPackingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Packing item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.textViewPartName.setText(item.PartName);
            binding.textViewPartSpecName.setText(item.PartSpec);
            binding.textViewEA2.setText(numFormatter.format(item.StockCommitted));//요청
            binding.textViewEA.setText(numFormatter.format(item.PackingQty));//포장


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
                    sc.StockCommitNo = stockCommitNo;
                    sc.PartCode = item.PartCode;
                    sc.PartSpec = item.PartSpec;
                    commonViewModel.Get4("GetOneItemDataPacking", sc);

                }
            });

        }
    }

    public void addItem(Packing item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<Packing> items) {
        this.items = items;
    }

    public Packing getItem(int position) {
        return items.get(position);
    }

    public ArrayList<Packing> getItemList() {
        return items;
    }

    public void setItem(int position, Packing item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}
