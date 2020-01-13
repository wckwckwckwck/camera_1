package uploadPhoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.camera_1.R;

import java.io.File;

public class UploadMain extends Activity implements OnClickListener {
	private static String URL = "http://116.255.232.223:4000/index/";
	private Button selectImage, uploadImage, takeImage;
	private ImageView imageView;
	String request;
	private String picPath = null;
	private Handler handler  = new Handler()
	{
		
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case WebService.CON_STATE:
				Toast.makeText(UploadMain.this, ""+msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload);
		selectImage = (Button) this.findViewById(R.id.selectImage);
		uploadImage = (Button) this.findViewById(R.id.uploadImage);
		takeImage = (Button) this.findViewById(R.id.takeImage);
		imageView = (ImageView) this.findViewById(R.id.imageView);
		selectImage.setOnClickListener(this);
		uploadImage.setOnClickListener(this);
		takeImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectImage:
			Intent intent = new Intent();// intent���Թ���ͼƬ�ļ���������
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, 22);
			break;
		case R.id.takeImage:
			Intent intent2 = new Intent(getApplicationContext(),
					TakePhotoActivity.class);
			startActivityForResult(intent2, 23);
			// �������ϴ�ͼƬ�ĵ�����ϴ����ˡ���
		case R.id.uploadImage:
			if (picPath == null) {
				Toast.makeText(UploadMain.this, "��ѡ��ͼƬ��", 1000).show();
			} else {
				
				new Thread() {	
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						Message msg = new Message();						
						final File file = new File(picPath);
						
						TextUploadUtil webService = new TextUploadUtil(handler);
						if (file != null) {
							webService.uploadFile(file, URL);
							
						}
						
/*						if(file.exists())
						{
							WebService webService = new WebService(URL,handler);
							MultipartEntity entity = new MultipartEntity();
							entity.addPart("userfile", new FileBody(file,file.getAbsolutePath(), "image/jpeg", "UTF-8")); 
							webService.post(entity, "0000;121a10c29912a912b12e61207122f1082e;A");
						}
*/						
						
					};
				}.start();// Thread()����

			}//eles����
			break;
		default:
			break;
		}// onclick����

	}// oncreate����

	// ���ز���ͼƬ�����յĽ��
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 22:
				Uri uri = data.getData();
				Log.e("Upload��ҳ---L92--������ͼƬ---------", "uri = " + uri);
				try {
					String[] pojo = { MediaStore.Images.Media.DATA };
					Cursor cursor = managedQuery(uri, pojo, null, null, null);
					if (cursor != null) {
						int colunm_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String path = cursor.getString(colunm_index);
						if (path.endsWith("jpg") || path.endsWith("png")) {
							picPath = path;
							ContentResolver cr = this.getContentResolver();
							Bitmap bitmap = BitmapFactory.decodeStream(cr
									.openInputStream(uri));
							imageView.setImageBitmap(bitmap);
						} else {
							alert();
						}
					} else {
						alert();
					}
				} catch (Exception e) {
				}
				break;
			case 23:
				imageView.setImageBitmap(null);// ����ͼƬλͼ����null�Ļ�,
												// ��һ���ϴ���Ƭ�ɹ��󣬵ڶ����ϴ���Ƭ�ᱨ��
				picPath = data.getStringExtra("photo_path");// ��SelectPicActivity.java�д���ͼƬ·��
				Log.i("uploadImage", "����ѡ���ͼƬ·��=" + picPath);
				Bitmap bm = BitmapFactory.decodeFile(picPath);// �ѻ�õ�ͼƬ·�������λͼ
				imageView.setImageBitmap(bm);// ��λͼ��ʾ����
				break;

			}// switch����
		}// if����
	}// onActivityResult����

	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("��ʾ")
				.setMessage("��ѡ��Ĳ�����Ч��ͼƬ")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						picPath = null;
					}
				}).create();
		dialog.show();
	}

}
