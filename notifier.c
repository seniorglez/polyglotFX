// notifier.c
#include <graalvm/llvm/polyglot.h>
#include <libnotify/notify.h>
int *notify_message() {
	notify_init ("New Message!");
	NotifyNotification * Hello = notify_notification_new ("Alan", "Hola amigo.", "dialog-information");
	notify_notification_show (Hello, NULL);
	g_object_unref(G_OBJECT(Hello));
	notify_uninit();
	return 0;
}