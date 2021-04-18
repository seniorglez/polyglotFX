// notifier.c
#include <graalvm/llvm/polyglot.h>
#include <libnotify/notify.h>

void *notify_message(const void *name, const void *message) {
	if(polyglot_is_string(name) && polyglot_is_string(message)){
		char *buffer_name, *buffer_message;
		buffer_name = (char*) malloc(polyglot_get_string_size(name)*sizeof(char));
		buffer_message = (char*) malloc(polyglot_get_string_size(message)*sizeof(char));
		polyglot_as_string(name,buffer_name,polyglot_get_string_size(name),"utf-8");
		polyglot_as_string(message,buffer_message,polyglot_get_string_size(message),"utf-8");
		
		notify_init ("New Message!");
		NotifyNotification * notification = notify_notification_new (buffer_name, buffer_message, "dialog-information");
		notify_notification_show (notification, NULL);
		g_object_unref(G_OBJECT(notification));
		notify_uninit();
		free(buffer_name);
		free(buffer_message);
	}
}