Ϊʲô FileIinputstream ��filereader����ô�ࣿ
FileIinputstream readʹ�õ���private native int readBytes(byte b[], int off, int len) throws IOException;
filereader ʹ�õ���StreamDecoder��read����
public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = paramInt1;
    int j = paramInt2;
    synchronized (this.lock)
    {
     ensureOpen();
    	...
   }