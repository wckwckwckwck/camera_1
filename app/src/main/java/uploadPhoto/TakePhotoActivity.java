package uploadPhoto;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.camera_1.R;
//ѡ�������ļ�������
public class TakePhotoActivity extends Activity implements OnClickListener{	
	private LinearLayout dialogLayout;//ȡ������
	private Button takePhotoBtn;//�������
	private Button cancelBtn;
	private Uri photoUri;//�Զ���ȡ��Ƭuri
	private String picPath;//ͼƬ·��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_pic_layout);
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
		dialogLayout.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);
		takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
		takePhotoBtn.setOnClickListener(this);
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_layout:
			finish();
			break;
		case R.id.btn_take_photo:
			takePhoto();
			break;
		//�ɵ���case��û�г�����״̬,��cancelBtn
		default:
			finish();
			break;
		}
	}
	// �������
	private void takePhoto() {
		//ִ������ǰ��Ӧ�����ж�SD���Ƿ����
		String SDState = Environment.getExternalStorageState();
		if(SDState.equals(Environment.MEDIA_MOUNTED))//�����ý�尲װ�Ļ���
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//���ĵ���
			/***
			 * ��Ҫ˵��һ�£����²���ʹ��+���գ����պ��ͼƬ����������е�
			 * ����ʹ�õ����ַ�ʽ��һ���ô����ǻ�ȡ��ͼƬ�����պ��ԭͼ
			 * �����ʹ��ContentValues�����Ƭ·���Ļ������պ��ȡ��ͼƬΪ����ͼ������
			 */
			ContentValues values = new ContentValues();//contentProvider�洢��
			photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);//�Զ����uri
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			/**-----------------*/
			startActivityForResult(intent, 1);//��������ҳ�棬����1û�õ���������һ��onActivityResult������requestCodeΪ0��������ҳ������ݡ�
		}else{
			Toast.makeText(this,"�ڴ濨������", Toast.LENGTH_LONG).show();
		}
	}	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//���������������û�ã�û�������µ�ҳ�洫ֵ����
		if(resultCode == Activity.RESULT_OK&&requestCode==1)//&&�ĺ�����п��ޣ���Ϊֻ��һ�����ؽ����������Ϳ���switch case�ж�һ�¡�
		{
			String[] pojo = {MediaStore.Images.Media.DATA};
			Cursor cursor = managedQuery(photoUri, pojo, null, null,null);//Uri �� ͼƬ����   �õ�  picPath
			if(cursor != null ){
				int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
				cursor.moveToFirst();
				picPath = cursor.getString(columnIndex);
				cursor.close();
			}
			Log.i("SelectPicActivity---------------", "imagePath = "+picPath);
			/*if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))*/
			if(picPath !=null){
				Intent lastIntent = getIntent();//����putExtra ����getIntent
				lastIntent.putExtra("photo_path", picPath);//������ҳ�棬���ٴ���·��  ���ͻ�ȥEmployeeAttendanceActivity
				setResult(Activity.RESULT_OK, lastIntent);
				finish();
			}else{
				Toast.makeText(this, "ѡ���ļ�����ȷ!", Toast.LENGTH_LONG).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
