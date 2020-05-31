package com.labour.lar.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.labour.lar.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DialogUtil {

	public static interface OnDialogListener<T> {

		public void onPositiveButtonClick(T t);

		public void onNegativeButtonClick(T t);
	}

	public static class OnDialogEvent<T> implements OnDialogListener<T> {
		@Override
		public void onPositiveButtonClick(T t) {
		}

		@Override
		public void onNegativeButtonClick(T t) {
		}
	}

	public static void showTextDialog(Context context, String title, String msg, boolean isAlertDialog, final OnDialogListener<Void> onDialogListener) {

		final AlertDialog adlg = new Builder(context).create();
		adlg.show();
		Window window = adlg.getWindow();
		window.setContentView(R.layout.widget_alert_dialog_view);

		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				if (id == R.id.cancel_btn) {
					if (onDialogListener != null) {
						onDialogListener.onNegativeButtonClick(null);
					}
				} else if (id == R.id.ok_btn) {
					if (onDialogListener != null) {
						onDialogListener.onPositiveButtonClick(null);
					}
				}
				adlg.dismiss();
			}
		};

		Button ok_btn = (Button) window.findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(clickListener);

		View btn_span_v = window.findViewById(R.id.btn_span_v);
		Button cancel_btn = (Button) window.findViewById(R.id.cancel_btn);
		if (isAlertDialog) {
			cancel_btn.setVisibility(View.GONE);
			btn_span_v.setVisibility(View.GONE);
		} else {
			cancel_btn.setVisibility(View.VISIBLE);
			btn_span_v.setVisibility(View.VISIBLE);
			cancel_btn.setOnClickListener(clickListener);
		}

		TextView title_tv = (TextView) window.findViewById(R.id.dialog_title_tv);
		TextView content_tv = (TextView) window.findViewById(R.id.dialog_content_tv);
		title_tv.setText(title);
		content_tv.setText(msg);
	}
	
	public static void showEditTextDialog(Context context, String title, String msg, final OnDialogListener<String> onDialogListener) {

		final AlertDialog adlg = new Builder(context).create();
		adlg.show();
		Window window = adlg.getWindow();
		window.setContentView(R.layout.widget_edit_dialog_view);
		window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		final EditText content_tv = (EditText) window.findViewById(R.id.dialog_content_tv);
		content_tv.setFocusable(true);
		
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				if (id == R.id.cancel_btn) {
					if (onDialogListener != null) {
						onDialogListener.onNegativeButtonClick(content_tv.getText().toString());
					}
				} else if (id == R.id.ok_btn) {
					if (onDialogListener != null) {
						onDialogListener.onPositiveButtonClick(content_tv.getText().toString());
					}
				}
				adlg.dismiss();
			}
		};

		Button ok_btn = (Button) window.findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(clickListener);

		Button cancel_btn = (Button) window.findViewById(R.id.cancel_btn);
		
		cancel_btn.setOnClickListener(clickListener);

		TextView title_tv = (TextView) window.findViewById(R.id.dialog_title_tv);
		title_tv.setText(title);

	}
	public static void showAlertDialog(Context context, String title, String msg, final OnDialogListener<Void> onDialogListener) {
		showTextDialog(context, title, msg, true, onDialogListener);
	}

	public static void showConfirmDialog(Context context, String title, String msg, final OnDialogListener<Void> onDialogListener) {
		showTextDialog(context, title, msg, false, onDialogListener);
	}
	
	public static void showDateDialog(Context mContext, Calendar defaultCalendar, final OnDialogListener<Calendar> onDialogListener) {
		if (defaultCalendar == null) {
			defaultCalendar = Calendar.getInstance();
		}
		int year = defaultCalendar.get(Calendar.YEAR);
		int moth = defaultCalendar.get(Calendar.MONTH);
		int day = defaultCalendar.get(Calendar.DAY_OF_MONTH);
		
		new DatePickerDialog(mContext, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.MONTH, monthOfYear);
				cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				if(onDialogListener != null){
					onDialogListener.onPositiveButtonClick(cal);
				}
			}
		}, year, moth, day).show();// 调用
	}
	
	public static void showTimeDialog(Context mContext, Calendar defaultCalendar, final OnDialogListener<Calendar> onDialogListener) {
		if (defaultCalendar == null) {
			defaultCalendar = Calendar.getInstance();
		}
		int hour = defaultCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = defaultCalendar.get(Calendar.MINUTE);
		
		new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
				if(onDialogListener != null){
					onDialogListener.onPositiveButtonClick(cal);
				}
			}
		},hour, minute, true).show();// 调用
	}
	
	public static void showDateTimeDialog(Context mContext, Calendar defaultCalendar, final OnDialogListener<Calendar> onDialogListener) {
		if (defaultCalendar == null) {
			defaultCalendar = Calendar.getInstance();
		}
		int hour = defaultCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = defaultCalendar.get(Calendar.MINUTE);
		
		new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
				if(onDialogListener != null){
					onDialogListener.onPositiveButtonClick(cal);
				}
			}
		},hour, minute, true).show();// 调用
	}
	
	/**
	 * 单选list
	 * @param context
	 * @param title
	 * @param menus
	 * @param onDialogListener
	 * @return
	 */
	public static AlertDialog showSingleChoiceDialog(Context context, String title, List<Menu> menus, final OnDialogListener<Menu> onDialogListener) {

		View view = View.inflate(context, R.layout.view_dialog_list, null);

		Builder builder = new Builder(context);
		final AlertDialog alertDialog = builder.show();
		
		Window window = alertDialog.getWindow();
		window.setContentView(view);
		
		final DialogListItemAdapter itemAdapter = new DialogListItemAdapter(context);
		itemAdapter.setList(menus);
		
		TextView titleView = (TextView) view.findViewById(R.id.textview_alert_title);
		titleView.setText(title);

		ListView listView = (ListView) view.findViewById(R.id.textview_alert_text);
		listView.setAdapter(itemAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				alertDialog.dismiss();
				if (onDialogListener != null) {
					Menu menu = (Menu)itemAdapter.getItem(position);
					onDialogListener.onPositiveButtonClick(menu);
				}
			}
		});
		
		return alertDialog;
	}

	public static class Menu {
		public int id;
		public String name;
		public int color;
		public int top;
		public int bottom;
		public int left;
		public int right;
		public int bgDrawable;
		public Object tag;

		public Menu(int id, String name) {
			this(id, name, R.color.black);
		}

		public Menu(int id, String name, int color) {
			this(id, name, color, 0, 0, 0, 0);
		}

		public Menu(int id, String name, int color, int top, int bottom, int left, int right) {
			this.id = id;
			this.name = name;
			this.color = color;
			this.top = top;
			this.bottom = bottom;
			this.left = left;
			this.right = right;
		}

		public Menu(int id, String name, int color, int top, int bottom, int left, int right, int bgDrawable) {
			this.id = id;
			this.name = name;
			this.color = color;
			this.top = top;
			this.bottom = bottom;
			this.left = left;
			this.right = right;
			this.bgDrawable = bgDrawable;
		}

		public void setTag(Object tag) {
			this.tag = tag;
		}

		public Object getTag() {
			return tag;
		}
	}

    static class DialogListItemAdapter  extends android.widget.BaseAdapter {

        protected Context mContext;
        protected LayoutInflater mInflater;
        protected List<Menu> items = new ArrayList<Menu>();

        public DialogListItemAdapter(Context context) {
            this.mContext = context;
            this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        public void setList(List<Menu> datas){
            this.items.clear();
            if(datas != null){
                this.items.addAll(datas);
            }
        }
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Menu getItem(int position) {
            return (Menu)items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = this.mInflater.inflate(R.layout.dialog_list_view_item, null);
            TextView name_tv = (TextView) view.findViewById(R.id.name_tv);

            Menu info = getItem(position);
            name_tv.setText(info.name);

            return view;
        }

    }
}
