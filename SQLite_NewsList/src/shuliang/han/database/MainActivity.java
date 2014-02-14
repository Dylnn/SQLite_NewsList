package shuliang.han.database;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {

	private SQLiteDatabase db;	//���ݿ����
	private ListView listView;	//�б�
	private EditText et_tittle;	//��������ű���
	private EditText et_content;	//�������������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//�򿪻��ߴ������ݿ�, �����Ǵ������ݿ�
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/news.db", null);
		System.out.println(this.getFilesDir().toString() + "/news.db");

		//��ʼ�����
		listView = (ListView) findViewById(R.id.lv_news);
		et_tittle = (EditText) findViewById(R.id.et_news_tittle);
		et_content = (EditText) findViewById(R.id.et_news_content);
		
		
	}
	
	/*
	 * �������ݵ����ݿ��еĴ�������¼�
	 * ������ݿ���ھ��������������ݿ�, ��������ڷ������ݿ��ʱ��ͻ���� SQLiteException �쳣
	 * �������� : ��ȡ��������ű��� �� ��������, ������ �� ���ݲ��뵽���ݿ�, ���»�ȡCursor, ʹ��Cursorˢ��ListView����
	 * �쳣���� : ������ʳ�����SQLiteException�쳣, ˵�����ݿⲻ����, ��ʱ����Ҫ�ȴ������ݿ�
	 */
	public void insertNews(View view) {
		String tittle = et_tittle.getText().toString();
		String content = et_content.getText().toString();
		
		try{
			insertData(db, tittle, content);
			Cursor cursor = db.rawQuery("select * from news_table", null);
			inflateListView(cursor);
		}catch(SQLiteException exception){
			db.execSQL("create table news_table (" +
					"_id integer primary key autoincrement, " +
					"news_tittle varchar(50), " +
					"news_content varchar(5000))");
			insertData(db, tittle, content);
			Cursor cursor = db.rawQuery("select * from news_table", null);
			inflateListView(cursor);
		}
		
	}
	
	/*
	 * �����ݿ��в�������
	 * �������� : 
	 * -- ������ : SQL���, ����������ʹ�� ? ��Ϊռλ��, ռλ���е������ں�����ַ����а���˳������滻
	 * -- ������ : �滻��������ռλ���е�����
	 */
	private void insertData(SQLiteDatabase db, String tittle, String content) {
		db.execSQL("insert into news_table values(null, ?, ?)", new String[]{tittle, content});
	}
	
	/*
	 * ˢ�����ݿ��б���ʾ
	 * 1. ����SimpleCursorAdapter�����ݿ��, ��ȡ���ݿ���е���������
	 * 2. �����µ�SimpleCursorAdapter���ø�ListView
	 */
	private void inflateListView(Cursor cursor) {
		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
				getApplicationContext(), 
				R.layout.item, 
				cursor, 
				new String[]{"news_tittle", "news_content"}, 
				new int[]{R.id.tittle, R.id.content});
		
		listView.setAdapter(cursorAdapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//��Activity���ٵ�ʱ��, ���û��
		if(db != null && db.isOpen())
			db.close();
	}
	
}
