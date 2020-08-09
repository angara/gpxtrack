# GPX Track: Web UI



## Telegram login

https://core.telegram.org/widgets/login

```javascript

<script async src="https://telegram.org/js/telegram-widget.js?11" 
  data-telegram-login="gpxtrack_bot" 
  data-size="medium" 
  data-radius="10" 
  data-onauth="onTelegramAuth(user)" 
  data-request-access="write"></script>

<script type="text/javascript">
  function onTelegramAuth(user) {
    alert('Logged in as ' + user.first_name + ' ' + user.last_name + 
          ' (' + user.id + (user.username ? ', @' + user.username : '') + ')');
  }

</script>
```

## misc

### cljs svg icons

https://github.com/status-im/status-react/blob/d692ddc7d9c80699eebead3a2434942fc59a0257/src/status_im/utils/slurp.clj#L20-L53
https://github.com/status-im/status-react/blob/d692ddc7d9c80699eebead3a2434942fc59a0257/src/status_im/ui/components/icons/vector_icons.cljs#L28

