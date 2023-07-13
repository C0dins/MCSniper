package net.mcsniper.tasks;

import net.mcsniper.Main;
import net.mcsniper.utils.APIUtil;
import net.mcsniper.utils.LogType;
import net.mcsniper.utils.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RequestRunnable implements Runnable {
    private long droptime, id;
    private String token, proxy, name, email;
    private int port;

    public RequestRunnable(long id, String name, long droptime, String email, String token, String proxy, int port){
        this.id = id;
        this.name = name;
        this.droptime = droptime;
        this.email = email;
        this.token = token;
        this.proxy = proxy;
        this.port = port;
    }
    @Override
    public void run() {
        Proxy prox = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(proxy, port));
        OkHttpClient client = new OkHttpClient.Builder().proxy(prox).build();
        Request request = new Request.Builder().url("https://api.minecraftservices.com/minecraft/profile/name/" + name)
                .addHeader("Authorization", "Bearer " + token)
                        .put(RequestBody.create(null, "")).build();
        SniperTask task = Main.getManager().getTasks().get((int) id - 1);
        task.getTesting().add(request.header("Authorization"));
        // Actual time to send request
        long sleepTillTime = (long) ((droptime - Instant.now().getEpochSecond() - 0.8) * 1000);
        try {
            Thread.sleep(sleepTillTime);
            if(task.isCanceled()){
                return;
            }

            Response response = client.newCall(request).execute();
            long send = response.sentRequestAtMillis();
            long recv = response.receivedResponseAtMillis();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            ZonedDateTime sendT = Instant.ofEpochMilli(send).atZone(ZoneId.systemDefault());
            ZonedDateTime recvT = Instant.ofEpochMilli(recv).atZone(ZoneId.systemDefault());

            String sendTime = sendT.format(dateFormatter);
            String recvTime = recvT.format(dateFormatter);

            int code = response.code();
            Logger.log("[%code%] %send% ---> %recv%".replace("%code%", String.valueOf(code)).replace("%send%", sendTime).replace("%recv%", recvTime));
            if(response.code() == 200){
                Logger.log(LogType.SUCCESS, "Successfully sniped " + name + " onto " + email);
                // DB Success
                Main.getDatabaseManager().successfulSnipe(name);
                Main.getDatabaseManager().successAccount(email);

                // Print success
                task.setSuccessEmail(email);
                if(Main.getConfigManager().isSkinchange()){
                    APIUtil.changeSkin(token);
                }

                if(Main.getConfigManager().isAnnounce()){
                    Main.getWebhookManager().announceSnipe(name);
                }

            }
            Main.getDatabaseManager().resumeAccount(email);
            Main.getSends().add(sendTime);
            Main.getRecvs().add(recvTime);
            Main.getCodes().add(code);
            response.close();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
