/**
 * �������������ע�����ˡ�
 */
package uploadPhoto;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * �ṩ������ʷ������
 * 
 */
public class WebService {

	// --------------------------------------------------
	// ----- constants -----
	// --------------------------------------------------
	private static final String TAG = "WebService";

	public static final int CON_STATE = 11;
	public static final int Servre_Response = 13;
	public static final int AUDIOFILE = 100;
	public static final int IMGFILE = 101;

	private int timeoutConnection = 30000;
	private int timeoutSocket = 5000;

	private static CookieStore stored_cookie = null;

	// --------------------------------------------------
	// ----- properties -----
	// --------------------------------------------------
	private Handler uihandler = null;
	private int responseCode = 0;
	private final String requestURL;

	// --------------------------------------------------
	// ----- extends -----
	// --------------------------------------------------
	/**
	 * �ṩ������ʹ��ܵ���--�������������Ķ���Ҫʹ�ø��࣬��Ҫ���ṩURL Ȼ���ٸ����ṩ�Ĳ����Ĳ�ͬ ��ִ�в�ͬ���������� �磺���ע������
	 * post(List<NameValuePair> params) ����ע������post(MultipartEntity
	 * userDefienEntity)
	 */
	public WebService(String URL, Handler handler) {
		this.requestURL = URL;
		this.uihandler = handler;

	}

	// --------------------------------------------------
	// ----- functions -----
	// --------------------------------------------------
	/**
	 * ���� --��������Ӧ�Ľ��
	 */
	public String post(List<NameValuePair> params, String header) {
		return post(params, timeoutConnection, header);
	}

	/**
	 * �����������HttpPost����
	 * 
	 * @param params
	 *            --����ȥ�Ĳ�������ʽ�Ǽ�ֵ�ԣ�NameValuePair
	 * @return ���ط�������Ӧ���ַ���
	 */
	public String post(List<NameValuePair> params, int httptimeout,
			String header) {

		if (this.requestURL == null) {
			return "Request fail: requestURL is null";
		}

		if (params == null) {
			return "Request fail: userDefine NameValuePair is null";
		}

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, httptimeout);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		final DefaultHttpClient httpclient = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(this.requestURL);

		if (header != null)
			httppost.addHeader("auth", header);

		String result = null;

		try {

			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			final HttpResponse httpresponse = httpclient.execute(httppost);
			final HttpEntity httpentity = httpresponse.getEntity();
			responseCode = httpresponse.getStatusLine().getStatusCode();

			if (httpentity != null) {
				result = EntityUtils.toString(httpentity, "UTF-8");
			}

			// Log.i(TAG, "responseCode is :" + responseCode);
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			TellConState(" in post(List<NameValuePair> params) UnsupportedEncodeingException");
			return null;
		} catch (final ClientProtocolException e) {

			e.printStackTrace();
			TellConState("in post(List<NameValuePair> params) ClientProtocolException");
			return null;

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TellConState("in post(List<NameValuePair> params) IOException");
			return null;
		}

		return result;

	}

	/**
	 * �����������HttpPost����
	 * 
	 * @param userDefineEntity
	 *            --����ȥ�Ĳ��������ļ�����ʽ��֯�ģ�MultipartEntity,add��ʱ�򣬶����ļ�����mimeType
	 * @return ���ط���������Ӧ���ַ���
	 * 
	 */
	public String post(MultipartEntity userDefineEntity, String header) {

		if (this.requestURL == null) {
			return "Request faile:requestURL is null";
		}

		if (userDefineEntity == null) {
			return "Request faile: userDefineEntity is null";
		}
		final HttpClient httpclient = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(this.requestURL);

		if (header != null)
			httppost.addHeader("auth", header);

		String result = null;

		try {

			httppost.setEntity(userDefineEntity);

			final HttpResponse httpresponse = httpclient.execute(httppost);

			final HttpEntity httpentity = httpresponse.getEntity();
			responseCode = httpresponse.getStatusLine().getStatusCode();

			Log.i(TAG, "responseCode is :" + responseCode);

			if (httpentity != null) {
				result = EntityUtils.toString(httpentity, "UTF-8");
			}

		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(TAG,
					" in post(MultipartEntity) UnsupportedEncodeingException");
			TellConState(" in post(MultipartEntity) UnsupportedEncodeingException");
			e.printStackTrace();
			return null;
		} catch (final ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "in post(MultipartEntity) ClientProtocolException");
			TellConState("in post(MultipartEntity) ClientProtocolException");
			e.printStackTrace();
			return null;
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "in post(MultipartEntity) IOException");
			TellConState("in post(MultipartEntity) IOException");
			e.printStackTrace();
			return null;
		}

		TellConState("Finish Uploading");
		return result;

	}

	/**
	 * ����ĳ�����������״̬��
	 * 
	 */
	public int getResoponseCode() {
		return responseCode;
	}

	/**
	 * ʹ��WebService.requestURL�ĵ�ַ���õ�ַָ��һ��ͼƬ�ļ����������ļ�
	 * 
	 * @return --����һ��HttpEntity����������ͼƬ���ݻ���������
	 * 
	 */
	private HttpEntity getEntity() {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(WebService.this.requestURL);
		try {
			HttpResponse httpresponse = httpclient.execute(httppost);
			HttpEntity entity = httpresponse.getEntity();
			WebService.this.responseCode = httpresponse.getStatusLine()
					.getStatusCode();

			return entity;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "in getEntity(), ClientProtocolException");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "in getEntity(),IOException");
			return null;
		}

	}

	/**
	 * �û�����Ҫ��ŵ�·����������Ŵӷ��������ص��ļ�
	 * 
	 * @param destFilePath
	 *            --���ļ����ݴ���ڸò�����ָ�����ļ���
	 * @return �ɹ����򷵻�true;���򷵻�false
	 */
	// public boolean getFile(String destFilePath) {
	//
	// try {
	// File file = createFile(destFilePath);
	// HttpEntity entity = getEntity();
	// if (file == null) {
	// Log.e(TAG, "�û��ֻ�SD��������");
	// return false;
	// }
	//
	// if (entity == null) {
	// Log.e(TAG, "������û�з�������");
	// return false;
	// }
	//
	// OutputStream out = null;
	// out = new BufferedOutputStream(new FileOutputStream(file));
	//
	// entity.writeTo(out);
	// out.close();
	//
	// return true;
	//
	// } catch (DirectoryCreatedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// Log.e(TAG, "�����ļ�Ŀ¼:" + destFilePath + ",ʧ��");
	// return false;
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// Log.e(TAG, "�ļ���" + destFilePath + ",������");
	// return false;
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// Log.e(TAG, "�����Է������� entityд��ָ���ļ��У����ִ���");
	// return false;
	// }
	//
	// }

	/**
	 * ��鵱ǰ�ֻ����ⲿ�洢���Ƿ����---�ⲿ�洢��δ�ض���SD�������󲿷ֶ���
	 */
	private boolean checkSDCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * ����·���������ļ�������д���ݣ������ɹ�������Է��أ�ʧ�ܣ����׳��쳣���߷���null
	 * 
	 * @throws com.yanhuo_01.compoments.WebService.DirectoryCreatedException
	 * 
	 */
	// private File createFile(String destFilePath)
	// throws com.yanhuo_01.compoments.WebService.DirectoryCreatedException {
	//
	// if (checkSDCard()) {
	// File file = new File(destFilePath);
	// if (!(file.mkdirs() || file.isDirectory())) {
	// Log.e(TAG, "chiefImage directory created failure");
	// throw new DirectoryCreatedException("�ļ�Ŀ¼����ʧ��");
	//
	// }
	// return file;
	// } else {
	// return null;
	// }
	//
	// }

	/**
	 * ��û�гɹ�����Ŀ¼��ʱ���׳�����쳣��Ķ���
	 */
	public class DirectoryCreatedException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String error = "";

		public DirectoryCreatedException(String error) {
			this.error = error;
		}

		public String getString() {
			return this.error;
		}
	}

	/**
	 * �����쳣���������������
	 * 
	 */
	private void TellConState(String str) {
		Message mesg = this.uihandler.obtainMessage();
		mesg.what = WebService.CON_STATE;
		mesg.obj = str;

		this.uihandler.sendMessage(mesg);

	}

	/**
	 * Ҫ���һ��Header
	 */
	public void performGet(String header) {

		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(requestURL);

		if (header != null)
			httpGet.addHeader("auth", header);

		String result = "";
		try {
			HttpResponse httpResponse = client.execute(httpGet);
			result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			Log.i(TAG, "result:" + result);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TellConState("ClientProtocolException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TellConState("IOException");
		}

		TellConState(result);

	}

}
