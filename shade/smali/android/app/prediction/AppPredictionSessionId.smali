.class public final Landroid/app/prediction/AppPredictionSessionId;
.super Ljava/lang/Object;
.source "AppPredictionSessionId.java"

# interfaces
.implements Landroid/os/Parcelable;


# annotations
.annotation runtime Landroid/annotation/SystemApi;
.end annotation


# static fields
.field public static final CREATOR:Landroid/os/Parcelable$Creator;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroid/os/Parcelable$Creator<",
            "Landroid/app/prediction/AppPredictionSessionId;",
            ">;"
        }
    .end annotation
.end field


# instance fields
.field private final mId:Ljava/lang/String;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    .line 76
    new-instance v0, Landroid/app/prediction/AppPredictionSessionId$1;

    invoke-direct {v0}, Landroid/app/prediction/AppPredictionSessionId$1;-><init>()V

    sput-object v0, Landroid/app/prediction/AppPredictionSessionId;->CREATOR:Landroid/os/Parcelable$Creator;

    return-void
.end method

.method private constructor <init>(Landroid/os/Parcel;)V
    .locals 0

    .line 44
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 45
    invoke-virtual {p1}, Landroid/os/Parcel;->readString()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Landroid/app/prediction/AppPredictionSessionId;->mId:Ljava/lang/String;

    return-void
.end method

.method synthetic constructor <init>(Landroid/os/Parcel;Landroid/app/prediction/AppPredictionSessionId$1;)V
    .locals 0

    .line 31
    invoke-direct {p0, p1}, Landroid/app/prediction/AppPredictionSessionId;-><init>(Landroid/os/Parcel;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;)V
    .locals 0

    .line 40
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 41
    iput-object p1, p0, Landroid/app/prediction/AppPredictionSessionId;->mId:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public describeContents()I
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public equals(Ljava/lang/Object;)Z
    .locals 2

    .line 50
    invoke-virtual {p0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v0

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v1

    goto :goto_0

    :cond_0
    const/4 v1, 0x0

    :goto_0
    invoke-virtual {v0, v1}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1

    const/4 p0, 0x0

    return p0

    .line 52
    :cond_1
    check-cast p1, Landroid/app/prediction/AppPredictionSessionId;

    .line 53
    iget-object p0, p0, Landroid/app/prediction/AppPredictionSessionId;->mId:Ljava/lang/String;

    iget-object p1, p1, Landroid/app/prediction/AppPredictionSessionId;->mId:Ljava/lang/String;

    invoke-virtual {p0, p1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public hashCode()I
    .locals 0

    .line 63
    iget-object p0, p0, Landroid/app/prediction/AppPredictionSessionId;->mId:Ljava/lang/String;

    invoke-virtual {p0}, Ljava/lang/String;->hashCode()I

    move-result p0

    return p0
.end method

.method public toString()Ljava/lang/String;
    .locals 0

    .line 58
    iget-object p0, p0, Landroid/app/prediction/AppPredictionSessionId;->mId:Ljava/lang/String;

    return-object p0
.end method

.method public writeToParcel(Landroid/os/Parcel;I)V
    .locals 0

    .line 73
    iget-object p0, p0, Landroid/app/prediction/AppPredictionSessionId;->mId:Ljava/lang/String;

    invoke-virtual {p1, p0}, Landroid/os/Parcel;->writeString(Ljava/lang/String;)V

    return-void
.end method
