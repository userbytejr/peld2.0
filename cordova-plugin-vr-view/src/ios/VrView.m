//
//  VrView.m
//  GoogleVrViewTest
//
//  Created by Leonardo Tonghini on 12/05/17.
//  Copyright © 2017 Leonardo Tonghini. All rights reserved.
//

#import "VrView.h"
#import "VideoVrVC.h"
#import "PanoramaVrVC.h"

@implementation VrView

- (void)playVideo:(CDVInvokedUrlCommand *)command {
    NSString *videoUrl = [command.arguments objectAtIndex:0];
    NSMutableDictionary *options = [command.arguments objectAtIndex:1];

    NSURL *remoteVideo = [NSURL URLWithString:videoUrl];
    [self startPlayingVideo: remoteVideo withOptions: options];
}

- (void)playVideoFromAppFolder:(CDVInvokedUrlCommand *)command {
    NSString *videoUrl = [command.arguments objectAtIndex:0];
    NSMutableDictionary *options = [command.arguments objectAtIndex:1];

    NSURL *localVideo = [self getLocalResourceUrl:videoUrl];
    [self startPlayingVideo: localVideo withOptions: options];
}

-(void)showPhoto:(CDVInvokedUrlCommand *)command {
    NSString *panoUrl = [command.arguments objectAtIndex:0];
    NSMutableDictionary *options = [command.arguments objectAtIndex:1];

    NSURL *remotePhoto = [NSURL URLWithString:panoUrl];
    [self startPlayingPhoto: remotePhoto withOptions: options];
}

-(void)showPhotoFromAppFolder:(CDVInvokedUrlCommand *)command {
    NSString *panoUrl = [command.arguments objectAtIndex:0];
    NSMutableDictionary *options = [command.arguments objectAtIndex:1];

    NSURL *localPhoto = [self getLocalResourceUrl:panoUrl];
    [self startPlayingPhoto: localPhoto withOptions: options];
}

-(void)isDeviceSupported:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:1];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)startPlayingVideo: (NSURL *) videoUrl withOptions: (NSMutableDictionary *) options {
    VideoVrVC *videoController = [[VideoVrVC alloc] init];
    videoController.videoURL = videoUrl;
    [videoController setOptions:options];
    [self.viewController presentViewController:videoController animated:YES completion:NULL];
}

-(void)startPlayingPhoto: (NSURL *) photoUrl withOptions: (NSMutableDictionary *) options {
    PanoramaVrVC *panoController = [[PanoramaVrVC alloc] init];
    panoController.panoramaURL = photoUrl;
    [panoController setOptions:options];
    [self.viewController presentViewController:panoController animated:YES completion:NULL];
}

-(NSURL *)getLocalResourceUrl:(NSString *)stringPath {
    if (stringPath != nil && stringPath.length > 0) {
        #ifdef __CORDOVA_4_0_0
            NSURL* baseUrl = [self.webViewEngine URL];
        #else
            NSURL* baseUrl = [self.webView.request URL];
        #endif
        NSURL* absoluteURL = [[NSURL URLWithString:stringPath relativeToURL:baseUrl] absoluteURL];

        if ([[NSFileManager defaultManager] fileExistsAtPath:absoluteURL.path]) {
            return absoluteURL;
        }
    }
    return nil;
}

@end
