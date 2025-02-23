using System.Reactive.PlatformServices;
using hotel_api_.RequestDto;
using Microsoft.AspNetCore.SignalR;
namespace hotel_api.Services;

public class EventHub:Hub
{
    public async Task SendMessage(string message)
    {
        await Clients.All.SendAsync("ReceiveMessage",message);
    }
}