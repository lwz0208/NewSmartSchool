package com.wust.newsmartschool.domain;


public class TrainningItem
{
    private String SKYX; //涓�璇鹃�㈢郴
    private String SKZY; //涓�璇句�涓�
    private String KCXZ; //璇剧��ц川
    private String KCH;  //璇剧���
    private String KCMC; //璇剧���绉�
    private String KKYX; //寮�璇鹃�㈢郴
    private String ZXS;  //�诲����
    private String XF;	 //瀛���
    private String JKXS; //璁茶�惧����
    private String SYXS; //瀹�楠�瀛���
    private String SJXS; //涓��哄����
    private String KSXQ; //寮�璁惧����
    private String FXMC;//��灞��瑰��
    private String HKFS;//���告�瑰�
    private String ZHXS;//�ㄥ����
    private String SHZT;//瀹℃�哥�舵��
    private String SortLetter;


    public TrainningItem(String sKYX, String sKZY, String kCXZ, String kCH,
                         String kCMC, String kKYX, String zXS, String xF, String jKXS,
                         String sYXS, String sJXS, String kSXQ, String fXMC, String hKFS,
                         String zHXS, String sHZT, String SortLetter)
    {
        super();
        SKYX = sKYX;
        SKZY = sKZY;
        KCXZ = kCXZ;
        KCH = kCH;
        KCMC = kCMC;
        KKYX = kKYX;
        ZXS = zXS;
        XF = xF;
        JKXS = jKXS;
        SYXS = sYXS;
        SJXS = sJXS;
        KSXQ = kSXQ;
        FXMC = fXMC;
        HKFS = hKFS;
        ZHXS = zHXS;
        SHZT = sHZT;
        this.SortLetter = SortLetter;
    }

    public TrainningItem()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    @Override
    public String toString()
    {
        return "TainningItem [SKYX=" + SKYX + ", SKZY=" + SKZY + ", KCXZ="
                + KCXZ + ", KCH=" + KCH + ", KCMC=" + KCMC + ", KKYX=" + KKYX
                + ", ZXS=" + ZXS + ", XF=" + XF + ", JKXS=" + JKXS + ", SYXS="
                + SYXS + ", SJXS=" + SJXS + ", KSXQ=" + KSXQ + "]";
    }
    public String getSKYX()
    {
        return SKYX;
    }
    public void setSKYX(String sKYX)
    {
        SKYX = sKYX;
    }
    public String getSKZY()
    {
        return SKZY;
    }
    public void setSKZY(String sKZY)
    {
        SKZY = sKZY;
    }
    public String getKCXZ()
    {
        return KCXZ;
    }
    public void setKCXZ(String kCXZ)
    {
        KCXZ = kCXZ;
    }
    public String getKCH()
    {
        return KCH;
    }
    public void setKCH(String kCH)
    {
        KCH = kCH;
    }
    public String getKCMC()
    {
        return KCMC;
    }
    public void setKCMC(String kCMC)
    {
        KCMC = kCMC;
    }
    public String getKKYX()
    {
        return KKYX;
    }
    public void setKKYX(String kKYX)
    {
        KKYX = kKYX;
    }
    public String getZXS()
    {
        return ZXS;
    }
    public void setZXS(String zXS)
    {
        ZXS = zXS;
    }
    public String getXF()
    {
        return XF;
    }
    public void setXF(String xF)
    {
        XF = xF;
    }
    public String getJKXS()
    {
        return JKXS;
    }
    public void setJKXS(String jKXS)
    {
        JKXS = jKXS;
    }
    public String getSYXS()
    {
        return SYXS;
    }
    public void setSYXS(String sYXS)
    {
        SYXS = sYXS;
    }
    public String getSJXS()
    {
        return SJXS;
    }
    public void setSJXS(String sJXS)
    {
        SJXS = sJXS;
    }
    public String getKSXQ()
    {
        return KSXQ;
    }
    public void setKSXQ(String kSXQ)
    {
        KSXQ = kSXQ;
    }

    public String getFXMC()
    {
        return FXMC;
    }

    public void setFXMC(String fXMC)
    {
        FXMC = fXMC;
    }

    public String getHKFS()
    {
        return HKFS;
    }

    public void setHKFS(String hKFS)
    {
        HKFS = hKFS;
    }

    public String getZHXS()
    {
        return ZHXS;
    }

    public void setZHXS(String zHXS)
    {
        ZHXS = zHXS;
    }

    public String getSHZT()
    {
        return SHZT;
    }

    public void setSHZT(String sHZT)
    {
        SHZT = sHZT;
    }

    public String getSortLetter()
    {
        return SortLetter;
    }

    public void setSortLetter(String sortLetter)
    {
        SortLetter = sortLetter;
    }




}

