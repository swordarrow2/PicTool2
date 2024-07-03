package com.meng.tools.ffmpeg;

import com.meng.app.*;

import java.io.*;

public class CommandBuilder {

    private File input;
    private File output;
    protected StringBuilder builder = new StringBuilder(MainActivity.instance.getFilesDir().getAbsolutePath() + File.separator + "ffmpeg");
//        -L license
//        -h 帮助
//        -fromats 显示可用的格式，编解码的，协议的。。。
//        -f fmt 强迫采用格式fmt
//        -I filename 输入文件
//        -y 覆盖输出文件
//        -t duration 设置纪录时间 hh:mm:ss[.xxx]格式的记录时间也支持
//        -ss position 搜索到指定的时间 [-]hh:mm:ss[.xxx]的格式也支持
//        -title string 设置标题
//        -author string 设置作者
//        -copyright string 设置版权
//        -comment string 设置评论
//        -target type 设置目标文件类型(vcd,svcd,dvd) 所有的格式选项（比特率，编解码以及缓冲区大小）自动设置 ，只需要输入如下的就可以了：
//        ffmpeg -i myfile.avi -target vcd /tmp/vcd.mpg
//        -hq 激活高质量设置
//        -itsoffset offset 设置以秒为基准的时间偏移，该选项影响所有后面的输入文件。该偏移被加到输入文件的时戳，
//        定义一个正偏移意味着相应的流被延迟了 offset秒。 [-]hh:mm:ss[.xxx]的格式也支持

    public CommandBuilder(File input) {
        this.input = input;
        builder.append(" -i ").append(input.getAbsolutePath()).append("");
    }

    public File getOutput() {
        return output;
    }

    public File getInput() {
        return input;
    }

    public CommandBuilder coverExistFile() {
        builder.append(" -y");
        return this;
    }

    public CommandBuilder title(String title) {
        builder.append(" -title \"").append(title).append("\"");
        return this;
    }

    public CommandBuilder author(String author) {
        builder.append(" -author \"").append(author).append("\"");
        return this;
    }

    public CommandBuilder copyright(String copyright) {
        builder.append(" -copyright \"").append(copyright).append("\"");
        return this;
    }

    public CommandBuilder comment(String comment) {
        builder.append(" -comment \"").append(comment).append("\"");
        return this;
    }

    public CommandBuilder target(Target target) {
        builder.append(" -target ").append(target.toString());
        return this;
    }

    public CommandBuilder highQuality() {
        builder.append(" -hq");
        return this;
    }

    public CommandBuilder itsoffset(float seconds) {
        builder.append(" -itsoffset ").append(String.format("%.2f", seconds));
        return this;
    }

    public CommandBuilder rawCmd(String cmd, String arg) {
        builder.append(" ").append(cmd).append(" ").append(arg);
        return this;
    }


    public CommandBuilder vcodec(String coder) {
        builder.append(" -vcodec ").append(coder);
        return this;
    }

    public CommandBuilder vf_scale(int width, int height) {
        builder.append(" -vf scale=").append(width).append(":").append(height);
        return this;
    }


//        -ab bitrate 设置音频码率
//        -ar freq 设置音频采样率
//        -ac channels 设置通道 缺省为1
//        -an 不使能音频纪录
//        -acodec codec 使用codec编解码

    public CommandBuilder bitrate(int kbps) {
        //bitrate
        builder.append(" -ab ").append(kbps);
        return this;
    }

    public CommandBuilder freq(int hz) {
        //freq
        builder.append(" -ar ").append(hz);
        return this;
    }

    public CommandBuilder channels(int channels) {
        //channels
        builder.append(" -ac ").append(channels);
        return this;
    }

    public CommandBuilder an() {
        builder.append(" -an");
        return this;
    }

    public CommandBuilder acodec(String coder) {
        builder.append(" -acodec ").append(coder);
        return this;
    }

    public String build(File output) {
        this.output = output;
        builder.append(" ").append(output.getAbsolutePath()).append("");
        return builder.toString();
    }


    public enum Target {
        vcd, svcd, dvd
    }
}
