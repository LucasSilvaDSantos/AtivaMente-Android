package com.example.ativamente.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ativamente.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private List<Date> dates = new ArrayList<>();
    private int selectedPosition = -1;
    private OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(Date date);
    }

    public void setOnDateClickListener(OnDateClickListener listener) {
        this.listener = listener;
    }

    public void setDates(List<Date> dates, Date selectedDate) {
        this.dates = dates;

        for (int i = 0; i < dates.size(); i++) {
            if (isSameDay(dates.get(i), selectedDate)) {
                selectedPosition = i;
                break;
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_item, parent, false);
        return new DateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        Date date = dates.get(position);
        holder.bind(date, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewDayOfWeek;
        private final TextView textViewDayOfMonth;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDayOfWeek = itemView.findViewById(R.id.text_view_day_of_week);
            textViewDayOfMonth = itemView.findViewById(R.id.text_view_day_of_month);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDateClick(dates.get(position));
                    notifyItemChanged(selectedPosition);
                    selectedPosition = position;
                    notifyItemChanged(selectedPosition);
                }
            });
        }

        void bind(Date date, boolean isSelected) {
            Locale ptBr = new Locale("pt", "BR");
            SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE", ptBr);
            SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("d", ptBr);

            textViewDayOfWeek.setText(dayOfWeekFormat.format(date).toUpperCase());
            textViewDayOfMonth.setText(dayOfMonthFormat.format(date));

            if (isSelected) {
                itemView.setBackgroundColor(itemView.getContext().getColor(R.color.light_green));
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
    
    private boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return fmt.format(date1).equals(fmt.format(date2));
    }
}
