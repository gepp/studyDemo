为什么 FileIinputstream 比filereader快这么多？
FileIinputstream read使用的是private native int readBytes(byte b[], int off, int len) throws IOException;
filereader 使用的是StreamDecoder的read方法
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