package com.team4runner.forrunner.Postergado_NaoUsado;

public class GcmIntentService /*extends IntentService*/ {
   /* // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String TAG = "gcm";

    public GcmIntentService() {
        super();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Log.i(TAG, "GcmIntentService.onHandleIntent: " + extras);
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        if (!extras.isEmpty()) {
            // Verifica o tipo da mensagem
            String messageType = gcm.getMessageType(intent);
            // O extras.isEmpty() precisa ser chamado para ler o bundle
            // Verifica o tipo da mensagem, no futuro podemos ter mais tipos
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                // Erro
                onError(extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Mensagem do tipo normal. Faz a leitura do par�metro "msg"
                // enviado pelo servidor
                onMessage(extras);
            }
        }
        // Libera o wake lock, que foi bloqueado pela classe
        // "GcmBroadcastReceiver".
        OldGcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void onError(Bundle extras) {
        Log.d(TAG, "Erro: " + extras.toString());
    }

    private void onMessage(Bundle extras) {
        // L� a mensagem e mostra uma notifica��o
        Log.i("1gcm", "entrou no metodo");

        String msg = extras.getString("mensagem");

        //    if (msg.equals("chat")) {

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

        Log.i("1gcm", prefs.getString("perfil", ""));

        if (prefs.getString("perfil", "").equals("treinador")) {

            Log.i("2gcm", "eh treinador");
            String origem = "treinador";
            ConsultaMensagensTask task = new ConsultaMensagensTask(this, prefs.getString("email", ""), origem);
            task.execute();

            try {
                List<Bundle> bundles = (List<Bundle>) task.get();

                int count = 1;
                for (Bundle bundle : bundles) {

                    Corredor corredor = (Corredor) bundle.get("corredor");
                    String qtdMsg = (String) bundle.get("qtdMsg");

                    Log.i("3gcm", "entrou no try");

                    Log.d(TAG, msg);
                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("msg", msg);
                    intent.putExtra("corredor", corredor);
                    intent.putExtra("origem", "treinador");
                    NotificationUtil.create(this, intent, corredor.getNome(), qtdMsg + " mensagem(ns) nao lida(s)", count);

                    count++;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        } else if (prefs.getString("perfil", "").equals("corredor")) {

            Log.i("2gcm", "eh corredor");
            String origem = "corredor";
            ConsultaMensagensTask task = new ConsultaMensagensTask(this, prefs.getString("email", ""), origem);
            task.execute();

            try {
                Bundle bundle = (Bundle) task.get();

                Treinador treinador = (Treinador) bundle.getSerializable("treinador");
                String qtdMsg = (String) bundle.get("qtdMsg");

                Log.i("3gcm", "entrou no try");

                Log.d(TAG, msg);
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("msg", msg);
                intent.putExtra("treinador", treinador);
                intent.putExtra("origem", "corredor");
                NotificationUtil.create(this, intent, treinador.getNome(), qtdMsg + " mensagem(ns) nao lida(s)", 1);


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }

    }*/
}