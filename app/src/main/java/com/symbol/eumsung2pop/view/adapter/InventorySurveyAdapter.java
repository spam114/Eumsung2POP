package com.symbol.eumsung2pop.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.symbol.eumsung2pop.R;
import com.symbol.eumsung2pop.databinding.RowInventoryBinding;
import com.symbol.eumsung2pop.model.SearchCondition;
import com.symbol.eumsung2pop.model.object.InventorySurvey;
import com.symbol.eumsung2pop.model.object.Users;
import com.symbol.eumsung2pop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InventorySurveyAdapter extends RecyclerView.Adapter<InventorySurveyAdapter.ViewHolder>  implements Filterable {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<InventorySurvey> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<InventorySurvey> unFilteredlist;//for filter
    ArrayList<InventorySurvey> filteredList;//for filter

    CommonViewModel commonViewModel;
    String itemTag;//마지막으로 추가된 태그

    public InventorySurveyAdapter(ArrayList<InventorySurvey> items, Context context, ActivityResultLauncher<Intent> resultLauncher, CommonViewModel commonViewModel) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.commonViewModel = commonViewModel;
    }


   /* public ProgressFloorReturnViewAdapter(Context context, LinearLayout layoutTop, String contractNo, String fromDate) {
        super();
        this.context = context;
        this.layoutTop = layoutTop;
        this.contractNo = contractNo;
        this.fromDate = fromDate;
    }*/

    public void updateAdapter(ArrayList<InventorySurvey> newCountries, String itemTag) {
        items.clear();
        items.addAll(newCountries);
        this.itemTag = itemTag;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowInventoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_inventory, viewGroup, false);
        return new InventorySurveyAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Customer item = items.get(position);
        InventorySurvey item = filteredList.get(position);//for filter
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
                    ArrayList<InventorySurvey> filteringList = new ArrayList<>();
                    for (InventorySurvey inventory : unFilteredlist) {
                        /*if (inventory.CustomerName.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(inventory);
                        }
                        if (inventory.LocationName.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(inventory);
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
                filteredList = (ArrayList<InventorySurvey>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        RowInventoryBinding binding;
        //View row;

        public ViewHolder(RowInventoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(InventorySurvey item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###");
            binding.txtItemTag.setText(item.ItemTag);
            binding.txtPartName.setText(item.PartName);
            binding.txtPartSpec.setText(item.PartSpecName);
            binding.txtQty.setText(numFormatter.format(item.Qty));
            binding.txtSeqNo.setText(Integer.toString(item.RowSeqNo));

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (item.StockOutNo.equals(""))
                        return;
                    CommonMethod.FNBarcodeConvertPrint(item.StockOutNo, barcodeConvertPrintViewModel);*/
                    //((InventorySurveyActivity)context).ScanStockOutTag((int)item.LocationNo);
                }
            });

            if(itemTag.equals(item.ItemTag)){//마지막으로 추가된 태그 배경색 변경
                binding.detailLayout.setBackgroundColor(Color.YELLOW);
            }
            else{
                binding.detailLayout.setBackgroundResource(R.drawable.borderline_bottom3);
            }

            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    MaterialAlertDialogBuilder alertBuilder= new MaterialAlertDialogBuilder(context);
                    alertBuilder.setTitle("TAG번호 \"" +(item.ItemTag+  "\"를 삭제 하시겠습니까?"));
                    final String[] selectedCoil = {""};;
                    alertBuilder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //삭제
                            deleteInventorySurvey(item.ItemTag);
                            dialog.dismiss();
                        }
                    });
                    alertBuilder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertBuilder.show();
                    return true;
                }
            });
        }
    }

    /**
     * 스캔한 TAG를 삭제한다.
     * @param itemTag
     */
    public void deleteInventorySurvey(String itemTag){
        SearchCondition sc = new SearchCondition();
        sc.ItemTag = itemTag;
        sc.SeqNo = Integer.toString(Users.SeqNo);
        commonViewModel.Get4("DeleteInventorySurvey", sc);
    }

    public void addItem(InventorySurvey item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<InventorySurvey> items) {
        this.items = items;
    }

    public InventorySurvey getItem(int position) {
        return items.get(position);
    }

    public ArrayList<InventorySurvey> getItemList() {
        return items;
    }

    public void setItem(int position, InventorySurvey item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }


}
